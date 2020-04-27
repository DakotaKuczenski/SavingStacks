package application.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Transaction.java class establishes a Transaction constructor that can be used to handle the 
 * transactions entered by the user via both the CashTransaction.fxml and the UploadFiles.fxml 
 * scenes. Methods in this class also allow for these objects to be retained in files for continued
 * use by the program.
 * 
 * @author Chelsea Flores (rue750)
 *
 */
public class Transaction {
	
	/*
	 * Class variables
	 */
	private int transId;
	private String entryDate;
	private String transDate;
	private String name;
	private String tag;
	private double amount;
	
	/*
	 * File variables set to access the transactions.csv and transId.csv files.
	 */
	private static String transFilename = "data/transactions.csv";
	private static String idFile = "transId.csv";
	private static String idFilename = "data/" + idFile;
	static File file;
	
	/**
	 * Transaction constructor for the transaction object.
	 * 
	 * @param idNumber - int of the ID number generated by the program.
	 * @param entryDate - String of the program generated date of the date that the transaction was entered.
	 * @param transDate - String of the date of the transaction, which is entered by the user.
	 * @param name - String of the title of the transaction
	 * @param tag - String of the tag or goal that the transaction is associated with.
	 * @param amount - double of the dollar amount of the transaction.
	 */
	public Transaction(int idNumber, String entryDate, String transDate, String name, String tag, double amount)
	{
		this.transId = idNumber;
		this.entryDate = entryDate;
		this.transDate = transDate;
		this.name = name;
		this.tag = tag;
		this.amount = amount;
	}
	
	/**
	 * saveTransaction takes a single transaction object and saves it to the appropriate file.
	 * @param transaction - Transaction object of a personal transaction that was created with user input.
	 */
	public static void saveTransaction( Transaction transaction )
	{
		Path path = Paths.get(transFilename);
		
		/*
		 * If the file exists, the file is appended. Otherwise, a new file is created.
		 */
		if( Files.exists(path))
		{
			appendTransToFile( transaction );
		}
		else
		{
			saveTransToNewFile(transaction);
		}
	}
	
	/**
	 * saveTransaction takes in an array of transaction objects and saves them to the appropriate file.
	 * 
	 * @param transArray - Transaction object array of a personal transactions that were created with user input.
	 */
	public static void saveTransaction( ArrayList<Transaction> transArray )
	{
		Path path = Paths.get(transFilename);
		
		/*
		 * If the file exists, the file is appended. Otherwise, a new file is created.
		 */
		if( Files.exists(path))
		{
			for( int i = 0; i < transArray.size(); i++ )
			{
				Transaction temp = transArray.get(i);
				appendTransToFile( temp );
			}
		}
		else
		{
			for( int i = 0; i < transArray.size(); i++ )
			{
				Transaction temp = transArray.get(i);
				saveTransToNewFile( temp );
			}		
		}
	}

	/**
	 * saveTransToNewFile saves a new file using the FileWriter which will overwrite the file if it already exists.
	 * 
	 * @param transaction - Transaction object array of a personal transactions that were created with user input.
	 */
	public static void saveTransToNewFile( Transaction transaction )
	{
		try {
			// open the file for writing	
			FileWriter writer = new FileWriter( new File( transFilename ) );		
			writer.write( transaction.toString() );
			writer.close();			
		}catch( IOException e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * appendTransToFile appends a file with the Transaction passed in.
	 * 
	 * @param transaction - Transaction object array of a personal transactions that were created with user input.
	 */
	public static void appendTransToFile( Transaction transaction )
	{
		try {
			// open the file for writing	
			FileWriter writer = new FileWriter( new File( transFilename ), true );		
			writer.write( transaction.toString() );
			writer.close();			
		}catch( IOException e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * establishTransId identifies if a transId file exists. If the file exists, then the int value
	 * is retrieved, incremented and returned. Otherwise, the transaction Id is set to 1.
	 * This allows us to keep track of the transaction Id without having to iterate through the all the user
	 * transactions to find the last save transaction Id.
	 * 
	 * @return id - int of the current transaction ID.
	 */
	public static int establishTransId()
	{
		Path path = Paths.get(idFilename);
		int id = 0;
		Scanner scan;
		
		if( Files.exists(path))
		{
			file = new File(idFilename);
			
			try {
				scan = new Scanner(file);
				String idString = scan.nextLine();
				String tokens[] = idString.split(",");
				id = Integer.parseInt(tokens[0]);
				id = id + 1;
				scan.close();
			} catch (FileNotFoundException e) {
			
				e.printStackTrace();
			}
		}
		else
		{
			id = 1;
		}
		saveTransId( id );	
		return id;
	}
	
	/**
	 * saveTransId takes the last transaction Id used and saves it into the transId.csv
	 * file for later use.
	 * 
	 * @param id - integer of the transaction Id
	 */
	public static void saveTransId( int id )
	{
		try {
			// open the file for writing	
			FileWriter writer = new FileWriter( new File( idFilename ) );
			String idString = String.valueOf(id);
			writer.write( idString );
			writer.close();			
		}catch( IOException e ) {
			e.printStackTrace();
		}
	}
	
	//delete a line in file, hope it works. 
	//delete from transaction arraylist. 
	/**
	 * Deletes a transaction from the list view from the transaction file.
	 * 
	 * @param deleteMe String is a string from the list view of the transaction that needs to be deleted.
	 */
	public static void deleter( String deleteMe) {
		
		String tokens[] = deleteMe.split("-");
		int idToDelete = Integer.parseInt(tokens[0].trim());
		
		ArrayList<Transaction> transFileLoad = null;
		
		try {
			transFileLoad = loadTransactions();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(transFileLoad == null)
			System.out.println("Transaction from the file did not load.");	      
	     
		for(int i = 0; i < transFileLoad.size(); i++)
		{
			Transaction temp = transFileLoad.get(i);
			if( temp.getTransId() == idToDelete )
			{
				transFileLoad.remove(i);
			}
			else
			{
				saveTransToNewFile( temp );
			}
		}
    
		/*
	      //Construct the new file that will later be renamed to the original filename.
	      BufferedReader br = new BufferedReader(new FileReader(inFile));
	      PrintWriter pw = new PrintWriter(new FileWriter(inFile));
	      String line = null;
	      //Read from the original file and write to the new
	      while ((line = br.readLine()) != null) 
	      {
	        if (!line.trim().equals(deleteMe)) 
	        {
	          pw.println(line);
	          pw.flush();
	        }
	      } //close files. 
	      pw.close();
	      br.close();
	      //Delete the og file
	      if (!inFile.delete()) 
	      {
	        System.out.println("Error deleting the file");
	        return;
	      }
	      //Rename the new file to the old file.
	      if (!tempF.renameTo(inFile))
	        System.out.println("Error renaming the file");*/
	  //  }catch (IOException ex) {
	  //    ex.printStackTrace();
	  //  }
	  }
	
	/**
	 * Loads the transaction from the transaction file into an arraylist.
	 * 
	 * @return ArrayList<Transaction> of transaction objects in the transaction file.
	 * @throws IOException
	 */
	public static ArrayList<Transaction> loadTransactions() throws IOException
	{
		ArrayList<Transaction> temp = new ArrayList<Transaction>();
		Scanner scan = new Scanner(new File( transFilename ));
		
		while( scan.hasNext())
		{
			String line = scan.nextLine();
			String tokens[] = line.split(",");
			int id = Integer.parseInt(tokens[0]);
			double amt = Double.parseDouble(tokens[4]);
			Transaction trans = new Transaction(id, tokens[1], tokens[3], tokens[2], tokens[5], amt );
			temp.add(trans);
			
		}
		
		scan.close();
		return temp;	
		
	}
	
	/**
	 * toStringList creates a string to be displayed on the listview.
	 * 
	 * @return ret - String of the transaction object.
	 */
	public String toStringList()
	{
		String ret = this.transId + " - " + this.tag + " - ";
		ret += this.transDate + " - " + this.name + " - " + this.amount;	
		return ret;
	}
	
	/**
	 * toString creates a string to be saved into the transaction file.
	 * 
	 * @return ret - String of the transaction object.
	 */
	public String toString()
	{
		String ret = this.transId + "," + this.entryDate + "," + this.name + "," + this.transDate + ",";
		ret += this.amount + "," + this.tag + "\n";
		return ret;
	}
	
	/**
	 * Returns the entry date of the transaction object.
	 * 
	 * @return the entryDate - String of the date the transaction was entered into the program.
	 */
	public String getEntryDate() {
		return entryDate;
	}

	/**
	 * Sets the entry date of the transaction object.
	 * 
	 * @param entryDate the entryDate to set as the date the transaction entered.
	 */
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	/**
	 * Returns the date of the transaction.
	 * 
	 * @return the transDate - String of the transaction date.
	 */
	public String getTransDate() {
		return transDate;
	}

	/**
	 * Sets the date of the transaction.
	 * 
	 * @param transDate the transDate to set String of the transaction date
	 */
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	/**
	 * Gets the transaction Id.
	 *
	 * @return the transId - String of the transaction Id.
	 */
	public int getTransId() {
		return transId;
	}

	/**
	 * Sets the transaction Id.
	 * 
	 * @param transId the transId to set String of the transaction Id.
	 */
	public void setTransId(int transId) {
		this.transId = transId;
	}



	/**
	 * Returns the title or name of the transaction.
	 * 
	 * @return the name - String of the transaction title.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the transaction.
	 * 
	 * @param name the name to set String of the transaction title.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the tag or goal associated with the transaction.
	 * 
	 * @return the tag - String of the goal associated with the transaction.
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the tag or goal associated with the transaction.
	 * 
	 * @param tag the tag to set a String of the tag or goal associated with the transaction
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	/**
	 * Returns the dollar amount of the transaction.
	 * 
	 * @return amount - double of the dollar amount of the transaction.
	 */
	public double getAmount()
	{
		return amount;
	}
	
	/**
	 * Sets the dollar amount of the transaction.
	 * 
	 * @param amount the amount to set double of the dollar amount of the transaction.
	 */
	public void setAmount( double amount )
	{
		this.amount = amount;
	}

}