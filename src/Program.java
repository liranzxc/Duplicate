import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;

public class Program {

	final static int SIZEIMG = 64;
	public static Map<String, String> Name_Key = null;
	public static Map<String, Integer> Key_Counter = null;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		long startTime = System.nanoTime();

		Name_Key = new ConcurrentHashMap<String, String>();
		Key_Counter = new ConcurrentHashMap<String, Integer>();

		MultiThreadPartOne();

		PartTwo_DeleteFile();

		System.out.println("size " + Name_Key.size());

		long endTime = System.nanoTime();

		long seconds = TimeUnit.NANOSECONDS.toSeconds(endTime - startTime);

		System.out.println("time " + seconds);

	}

	private static void MultiThreadPartOne() throws InterruptedException {
		// TODO Auto-generated method stub

		ExecutorService executor = Executors.newCachedThreadPool();
		String dir = "C:\\Users\\liran\\Desktop\\google-images-download\\google_images_download\\downloads\\Apple and Banana and Elephant";

		Iterator<File> directoryListing = FileUtils.iterateFiles(new File(dir),
				new String[] { "jpg", "png", "jpeg", "gif", "JPG", "PNG" }, true);

		while (directoryListing.hasNext()) {
			File child = directoryListing.next();
			executor.execute(new FileHashThread(child, Name_Key, Key_Counter));
		}

		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

	}

	private static void PartOne_FindallDuplicate(String dirname, HashMap<String, String> Name_Key,
			HashMap<String, Integer> Key_Counter) throws Exception {
		// TODO Auto-generated method stub

		File dir = new File(dirname);
		if (!dir.isDirectory()) {
			throw new Exception("Dir dont exists");
		}
		Iterator<File> directoryListing = FileUtils.iterateFiles(dir,
				new String[] { "jpg", "png", "jpeg", "gif", "JPG", "PNG" }, true);

		if (directoryListing != null) {
			// build a map
			while (directoryListing.hasNext()) {
				File child = directoryListing.next();
				String key = GetHashImage(ImageIO.read(child));
				Name_Key.put(child.getAbsolutePath(), key);

				if (Key_Counter.containsKey(key))
					Key_Counter.put(key, Key_Counter.get(key) + 1);
				else
					Key_Counter.put(key, 1);
			}
		}
	}

	private static void PartTwo_DeleteFile() {
		// TODO Auto-generated method stub

		for (String key : Key_Counter.keySet()) {
			if (Key_Counter.get(key) > 1) {

				// look for names
				System.out.println("Key group is " + key);
				ArrayList<String> nameFilesToDeleted = new ArrayList<>();
				for (String name : Name_Key.keySet()) {
					if (Name_Key.get(name).equals(key)) {
						// System.out.println("name:"+name);
						nameFilesToDeleted.add(name);
					}

				}
				nameFilesToDeleted.remove(nameFilesToDeleted.size() - 1);
				DeleteFile(nameFilesToDeleted);

			}
		}

	}

	private static void DeleteFile(ArrayList<String> nameFilesToDeleted) {
		// TODO Auto-generated method stub

		nameFilesToDeleted.forEach(name -> {

			System.out.println(name + " Deleted");
			new File(name).delete();

		});

	}

	private static String GetHashImage(BufferedImage img1) throws IOException {
		// resize any image to SIZEIMGXSIZEIMG pixel
		BufferedImage thumbnail1 = Scalr.resize(img1, SIZEIMG);

		BufferedImage result1 = new BufferedImage(SIZEIMG, SIZEIMG, BufferedImage.TYPE_BYTE_BINARY);

		Graphics2D graphic = result1.createGraphics();

		graphic.drawImage(thumbnail1, 0, 0, Color.WHITE, null);
		graphic.dispose();

		byte[] pixels = new byte[SIZEIMG * SIZEIMG];
		int index = 0;
		for (int x = 0; x < SIZEIMG; x++) {
			for (int y = 0; y < SIZEIMG; y++) {
				pixels[index] = (byte) (result1.getRGB(x, y) == 0xFFFFFFFF ? 0 : 1);
				index++;

			}

		}
		return Base64.getEncoder().encodeToString(pixels);
	}

}
