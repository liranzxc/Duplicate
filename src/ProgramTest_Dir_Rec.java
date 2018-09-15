import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

public class ProgramTest_Dir_Rec {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String dirname = "imgCompare";

		Iterator<File> x = FileUtils.iterateFiles(new File(dirname), new String[] {"jpg"}, true);

		while(x.hasNext())
		{
			System.out.println(x.next().getPath());
		}
	}

}
