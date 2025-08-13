package util;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
public class JsonReader {

	private static final ObjectMapper mapper = new ObjectMapper();
	public static <T> List<T> readJSON(String path, Class<T> clazz)
	{
		try {
			return mapper.readValue(new File(path),mapper.getTypeFactory().constructCollectionType(List.class, clazz));
		} catch (StreamReadException e) {
			e.printStackTrace();
		} catch (DatabindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
