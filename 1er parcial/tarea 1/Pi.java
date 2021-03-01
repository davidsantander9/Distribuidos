import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;
import java.nio.ByteBuffer;


class PI
{
  static Object lock = new Object();
  static double pi = 0;
  static class Worker extends Thread
  {
    Socket conexion;
    Worker(Socket conexion)
    {
      this.conexion = conexion;
    }
    public void run()
    {
      try {
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        double x = entrada.readDouble();
        Object obj = new Object();
        synchronized(obj)
        {
          pi += x;
        }
        salida.close();
        entrada.close();
        conexion.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
  }
  public static void main(String[] args) throws Exception
  {
    if (args.length != 1)
    {
      System.err.println("Uso:");
      System.err.println("java PI <nodo>");
      System.exit(0);
    }
    int nodo = Integer.valueOf(args[0]);
    if(nodo == 0)
    {
      ServerSocket servidor = new ServerSocket(50000);
      Worker[] w = new Worker[4];
      for (int i = 0; i < w.length; i++ ){
        Socket conexion = servidor.accept();
        w[i] =  new Worker(conexion);
        w[i].start();
      }

      for (int j = 0; j < w.length; j++) {
        w[j].join();
      }
      System.out.println("Valor de pi " + pi);
      
    }
    else
    {
      Socket conexion = null;
      for(;;)
      try
      {
    	  conexion = new Socket("localhost",50000);
          break;
      }
      catch (Exception e)
      {
        Thread.sleep(100);
      }

      DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
      DataInputStream entrada = new DataInputStream(conexion.getInputStream());
      Double suma = 0.0;
      for(int i = 0; i < 1000000; i ++ ){
        suma =  4.0/(8*i+2*(nodo-2)+3)+suma;
      }
      suma =  nodo%2==0?-suma:suma;
      salida.writeDouble(suma);
      salida.close();
      entrada.close();
      conexion.close();
    }
  }
}