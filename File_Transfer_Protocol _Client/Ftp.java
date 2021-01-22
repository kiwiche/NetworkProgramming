package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by user on 2017/5/23.
 */
public class Ftp {
    Socket ctrlSocket;
    public PrintWriter ctrlOutput;
    public BufferedReader ctrlInput;
    public ObservableList<String> file = FXCollections.observableArrayList();
    public ArrayList<String> state = new ArrayList<>();

    final int CTRLPORT = 21 ;

    public void openConnection(String host)
            throws IOException,UnknownHostException
    {
        ctrlSocket = new Socket(host, CTRLPORT);
        ctrlOutput = new PrintWriter(ctrlSocket.getOutputStream());
        ctrlInput
                = new BufferedReader(new InputStreamReader
                (ctrlSocket.getInputStream()));
    }

    public void closeConnection()
            throws IOException
    {
        ctrlSocket.close() ;
    }


    public void showMenu()
    {
        System.out.println(">Command?") ;
        System.out.print(" 1 login") ;
        System.out.print(" 2 ls") ;
        System.out.print(" 3 cd") ;
        System.out.print(" 4 get") ;
        System.out.print(" 5 put") ;
        System.out.println(" 9 quit") ;
    }


    public String getCommand()
    {
        String buf = "" ;
        BufferedReader lineread
                = new BufferedReader(new InputStreamReader(System.in)) ;
        while(buf.length() != 1){// １文字の入力を受けるまで繰り返し
            try{
                buf = lineread.readLine() ;

            }catch(Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        return (buf) ;
    }


    public void doLogin(String loginName,String password)
    {
        BufferedReader lineread
                = new BufferedReader(new InputStreamReader(System.in)) ;
        try{
            System.out.println("ログイン名を入力してください") ;

            // USERコマンドによるログイン
            ctrlOutput.println("USER " + loginName) ;
            ctrlOutput.flush() ;
            // PASSコマンドによるパスワードの入力
            System.out.println("パスワードを入力してください") ;

            ctrlOutput.println("PASS " + password) ;
            ctrlOutput.flush() ;
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void doQuit()
    {
        try{
            ctrlOutput.println("QUIT ") ;
            ctrlOutput.flush() ;

        }catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // doCdメソッド
    // ディレクトリを変更します
    public void doCd(String dirName,String selected)
    {
        BufferedReader lineread
                = new BufferedReader(new InputStreamReader(System.in)) ;

        try{

            if(selected.equals("d")) {
                ctrlOutput.println("CWD " + dirName) ;// CWDコマンド
                ctrlOutput.flush() ;
            }

        }catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // doLsメソッド
    // ディレクトリ情報を得ます
    public ObservableList<String> doLs()
    {
        file.clear();
        state.clear();

        file.add("..");
        state.add("d");

        try{
            int n ;
            byte[] buff = new byte[1024] ;
            // データ用コネクションを作成します
            Socket dataSocket = dataConnection("LIST") ;
            // データ読み取り用ストリームを用意します
            BufferedInputStream dataInput
                    = new BufferedInputStream(dataSocket.getInputStream()) ;
            // ディレクトリ情報を読み取ります
            while((n = dataInput.read(buff)) > 0){
                String string = new String(buff);

                String token[] = string.split("\\r?\\n");

                for(String toke:token)
                {
                    if(toke.charAt(0) == '-')
                        state.add("f");
                    if(toke.charAt(0) == 'd')
                        state.add("d");

                    //.substring(49)
                    file.add(toke);
                }
            }

            dataSocket.close() ;

        }catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return file;
    }

    // dataConnectionメソッド
    // サーバとのデータ交換用にソケットを作ります
    // また,サーバに対してportコマンドでポートを通知します
    public Socket dataConnection(String ctrlcmd)
    {
        String cmd = "PORT " ; //PORTコマンドで送るデータの格納用変数
        int i ;
        Socket dataSocket = null ;// データ転送用ソケット
        try{
            // 自分のアドレスを求めます
            byte[] address = InetAddress.getLocalHost().getAddress() ;
            // 適当なポート番号のサーバソケットを作ります
            ServerSocket serverDataSocket = new ServerSocket(0,1) ;
            // PORTコマンド用の送信データを用意します
            for(i = 0; i < 4; ++i)
                cmd = cmd + (address[i] & 0xff) + "," ;
            cmd = cmd + (((serverDataSocket.getLocalPort()) / 256) & 0xff)
                    + ","
                    + (serverDataSocket.getLocalPort() & 0xff) ;
            // PORTコマンドを制御用ストリームを通して送ります
            ctrlOutput.println(cmd) ;
            ctrlOutput.flush() ;
            // 処理対象コマンド（LIST,RETR,およびSTOR）をサーバに送ります
            ctrlOutput.println(ctrlcmd) ;
            ctrlOutput.flush() ;
            // サーバからの接続を受け付けます
            dataSocket = serverDataSocket.accept() ;
            serverDataSocket.close() ;
        }catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        return dataSocket ;
    }


    // doGetメソッド
    // サーバ上のファイルを取り込みます
    public void doGet(String fileName)
    {
        BufferedReader lineread
                = new BufferedReader(new InputStreamReader(System.in)) ;

        try{
            int n ;
            byte[] buff = new byte[1024] ;
            // サーバ上ファイルのファイル名を指定します
            System.out.println("Enter file name") ;
            // クライアント上に受信用ファイルを準備します
            FileOutputStream outfile = new FileOutputStream(fileName) ;
            // ファイル転送用データストリームを作成します
            Socket dataSocket = dataConnection("RETR " + fileName) ;
            BufferedInputStream dataInput
                    = new BufferedInputStream(dataSocket.getInputStream()) ;
            // サーバからデータを受け取り,ファイルに格納します
            while((n = dataInput.read(buff)) > 0){
                outfile.write(buff,0,n) ;
            }

            dataSocket.close() ;
            outfile.close() ;

        }catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // doPutメソッド
    // サーバへファイルを送ります
    public void doPut(String fileName)
    {
        BufferedReader lineread
                = new BufferedReader(new InputStreamReader(System.in)) ;
        try{
            int n ;
            byte[] buff = new byte[1024] ;
            // ファイル名を指定します
            System.out.println("ファイル名を入力してください") ;
            // クライアント上のファイルの読み出し準備を行います
            FileInputStream sendfile = new FileInputStream(fileName) ;
            // 転送用データストリームを用意します
            Socket dataSocket = dataConnection("STOR " + fileName) ;
            OutputStream outstr = dataSocket.getOutputStream() ;
            // ファイルを読み出し,ネットワーク経由でサーバに送ります
            while((n = sendfile.read(buff)) > 0){
                outstr.write(buff,0,n) ;
            }

            dataSocket.close() ;
            sendfile.close() ;

        }catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public void doDel(String remove)
    {
        ctrlOutput.println("DELE " + remove);
        ctrlOutput.flush();
    }


    public void doDir(String remove)
    {
        ctrlOutput.println("RMD " + remove);
        ctrlOutput.flush();
    }


    public void doCreate(String name)
    {
        ctrlOutput.println("MKD " + name);
        ctrlOutput.flush();
        ctrlOutput.flush();
    }

    // getMsgsメソッド
    // 制御ストリームの受信スレッドを開始します
    public void getMsgs(){
        try {
            CtrlListen listener = new CtrlListen(ctrlInput) ;
            Thread listenerthread = new Thread(listener) ;
            listenerthread.start() ;
        }catch(Exception e){
            e.printStackTrace() ;
            System.exit(1) ;
        }
    }

    // CtrlListen クラス
    class CtrlListen implements Runnable{
        BufferedReader ctrlInput = null ;
        // コンストラクタ読み取り先の指定
        public CtrlListen(BufferedReader in){
            ctrlInput = in ;
        }

        public void run(){
            while(true){
                try{ // ひたすら行を読み取り,標準出力にコピーします
                    System.out.println(ctrlInput.readLine()) ;
                } catch (Exception e){
                    System.exit(1) ;
                }
            }
        }
    }
}
#ppop
