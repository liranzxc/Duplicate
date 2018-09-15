import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class FileHashThread extends Thread {

	private File child;
	private static Map<String, String> Name_Key;
	private static Map<String, Integer> Key_Counter;
	private final static int SIZEIMG = 64;

	public FileHashThread(File child, final Map<String, String> name_Key2, final Map<String, Integer> key_Counter2) {
		this.child = child;
		Name_Key = name_Key2;
		Key_Counter = key_Counter2;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {

			String key = GetHashImage(ImageIO.read(child));

			Name_Key.put(child.getAbsolutePath(), key);
			if (Key_Counter.containsKey(key))
				Key_Counter.put(key, Key_Counter.get(key) + 1);
			else
				Key_Counter.put(key, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
