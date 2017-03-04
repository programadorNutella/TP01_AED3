import java.util.Scanner;
import java.io.*;

class Empresa
{
	static String str_tamanho( String palavra ) //Retorna o tamanho da string com 2 caracteres
	{
		if( palavra.length() < 10 )
			return "0" + palavra.length();
		else
			return "" + palavra.length();
	}
	
	
	static void questaoA() throws IOException
	{
		// Abrir arquivos
		Scanner sc = new Scanner( new File("empresa.txt") );
		FileOutputStream out = new FileOutputStream( "empresa_fix.txt");
		
		// Inicializar vetores
		byte[] buffer = new byte[232];
		int[] tamanho = { 5, 60, 12, 60, 10, 25, 25, 8, 25, 2 };
		
		// Ler e gravar arquivos
		while( sc.hasNextLine() )
		{
			int contador = 0;
			// Correcao: split estava funcionando de forma errada com '|', quebrando letra por letra
			String[] campos = sc.nextLine().replace( '|', '&' ).split( "&" );
			
			for ( int i = 0; i < tamanho.length; i++ )
			{
				byte[] campoaux = campos[i].trim().getBytes();
            try
				{
					System.arraycopy( campoaux, 0, buffer, contador, campoaux.length );
					contador += tamanho[i];
				}
				catch( ArrayIndexOutOfBoundsException e ) // Essa excessao e lancada ocasionalmente para cidades nao sei por que
				{
				   String correcao = "";
               for ( int k = 0; k < campoaux.length; k++ )
               {
                  // Percorre string com problemas lendo char por char e salva numa nova string
                  correcao += campos[i].charAt(k);
               }
               System.arraycopy( correcao.trim().getBytes(), 0, buffer, contador, 1 );
				}
			}
			
			out.write(buffer);
			buffer = new byte[232];
		}
		
		//Fechar arquivo
		out.close();
	}
	
	
	static void questaoB() throws IOException
	{
		// Abrir arquivos
		FileInputStream in = new FileInputStream("empresa_fix.txt");
		FileOutputStream out = new FileOutputStream( "empresa_len.txt");
		
		// Inicializar vetores
		byte[] buffer = new byte[232];
		int[] tamanho = { 5, 60, 12, 60, 10, 25, 25, 8, 25, 2 };
		
		// Le e grava arquivo
		while ( in.available() > 0 )
		{
			int contador = 0;
			in.read( buffer );
			for ( int i = 0; i < tamanho.length; i++ )
			{
				String dado = new String(buffer, contador, tamanho[i]).trim();
				out.write( (str_tamanho( dado ) + dado).getBytes() );
				contador+= tamanho[i];
			}
		}
		
		// Fechar arquivos
		in.close();
		out.close();
	}
	
	
	static void questaoC() throws IOException
	{
		// Abrir arquivos
		FileInputStream in = new FileInputStream("empresa_len.txt");
		FileOutputStream out = new FileOutputStream( "empresa_xml.txt");
		
		// Vetores
		byte[] buffer;
		String[] campos = { "id", "empresa", "telefone", "endereco", "numero", "complemento", "bairro", "cep", "cidade", "estado" };
		
		// Le e grava arquivo
		while ( in.available() > 0 )
		{
			out.write( "<dados>\n".getBytes() );
			for( int i = 0; i < campos.length; i++ )
			{
				buffer = new byte[2]; // Tamanho do contador de caracteres
				in.read( buffer );
				buffer = new byte [ Integer.parseInt ( new String(buffer, 0, 2).trim() ) ]; // Tamanho do valor
				in.read( buffer );
				out.write (("\t<" + campos[i] + ">" + new String( buffer, 0, buffer.length ).trim() + "</" + campos[i] + ">\n" ).getBytes());
			}
			out.write( "</dados>\n".getBytes() );
		}
		
		in.close();
		out.close();
	}
	
	
	public static void main ( String[] args )
	{
		try
		{
			questaoA();
			questaoB();
			questaoC();
		}
		catch ( IOException e )
		{
			System.out.print( "Erro: " + e.getMessage() );
		}
	}
	
}
