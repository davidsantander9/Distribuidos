import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

class Cliente
{
  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception
  {
    while (longitud > 0)
    {
      int n = f.read(b,posicion,longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception
  {
    Socket conexion = new Socket("localhost",50000);

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    // enva un entero de 32 bits
    salida.writeInt(123);

    // envia un numero punto flotante
    salida.writeDouble(1234567890.1234567890);

    // envia una cadena
    salida.write("hola".getBytes());

    // recibe una cadena
    byte[] buffer = new byte[4];
    read(entrada,buffer,0,4);
    System.out.println(new String(buffer,"UTF-8"));

    // envia 5 numeros punto flotante
    // Double num;
    // ByteBuffer b = ByteBuffer.allocate(1000*8);
    // for( int i = 0; i<1000; i++){
    //     num = (double) i;
    //     b.putDouble(num);
    // }
    Double num;
    for( int i=0; i<1000; i++){
        num = (double) i;
        salida.writeDouble(num);
    }
    
    
    Long time = System.currentTimeMillis();
    System.out.println(time);

    // byte[] a = b.array();
    // salida.write(a);

    salida.close();
    entrada.close();
    conexion.close();    
  }
}