import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.mozilla.universalchardet.UniversalDetector;

/**
 * 
 * @author icj
 *
 */
public class Encoder {

	public static void main(String[] args) throws IOException {
		System.out.println("Run main");
		// 프로젝트 폴더 경로
		decodingProjectSources(new File("C:\\workspace"));
		System.out.println("End main");
	}

	public static  void decodingProjectSources(File file) throws IOException {
		// 자바 파일만 UTF-8로 디코딩
		if(file.isFile() && file.getName().matches("^.*\\.((?i)JAVA)$")) {
			String encoding =  readEncoding(file);
			if(encoding != null && !encoding.equals("UTF-8")) {
				decodingFile(file, encoding);
			}
		} else if(file.isDirectory()) {
			File[] list = file.listFiles();
			for(File childFile : list) {
				decodingProjectSources(childFile);
			}
		}
	}

	public static void decodingFile(File file, String encoding) throws IOException {
		Charset charset = Charset.forName(encoding);
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream fbs = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[4096];
		int n = 0;
		while((n = fis.read(buffer, 0, buffer.length)) > 0) {
			fbs.write(buffer, 0, n);
		}
		CharBuffer charBuffer = charset.decode(ByteBuffer.wrap(fbs.toByteArray()));
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.append(charBuffer);
		bw.close();
	}

	public static String readEncoding(File file) throws IOException {
		byte[] buf = new byte[4096];
		java.io.FileInputStream fis = new java.io.FileInputStream(file);
		UniversalDetector detector = new UniversalDetector(null);
		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();
		detector.reset();
		buf = null;
		fis.close();
		return encoding == null?"UTF-8":encoding;
	}
}
