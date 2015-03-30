/*
 * Klasse ließt Textdatein(*.graph) ein die dem Format EBNF entsprechen
 */
package eu.haw.gkaprojects.duc.robert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jgraph.JGraph;

public class Readfile
{
	public void load(String filename)
	{
		File f = new File(filename + ".graph");

		boolean ex = f.exists();

		if (ex)
		{
			boolean re = f.canRead();
			if (re)
			{
				try
				{
					FileReader reader = new FileReader(f);
					BufferedReader input = new BufferedReader(reader);

					// TO Do

					input.close();
					reader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println("File kann nicht gelesen werden!");
			}
		}
		else
		{
			
			System.out.println("File existiert nicht");
		}
	}

	public JGraph getGraph()
	{
		JGraph j = new JGraph();
		return j;
	}
}
