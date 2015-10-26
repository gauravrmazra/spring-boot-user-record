package in.blogspot.javawithgaurav.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.EmptyIterator;

import in.blogspot.javawithgaurav.domain.User;


@Service
public class JsonUserServiceImpl implements UserService {
	private ReadWriteLock lock = new ReentrantReadWriteLock(true);
	private Lock readLock = lock.readLock();
	private Lock writeLock = lock.writeLock();
	
	
	private void writeDataInJsonFile(List<User> users) {
		writeLock.lock();
		try {
			JsonFactory jsonFactory = new JsonFactory(); 
			JsonGenerator jsonGen;
			try {
				FileOutputStream fos = new FileOutputStream("src/main/resources/users.json");
				jsonGen = jsonFactory.createGenerator(fos, JsonEncoding.UTF8);
				jsonGen.setPrettyPrinter(new DefaultPrettyPrinter());
				jsonGen.setCodec(new ObjectMapper());
				jsonGen.writeStartObject();
				jsonGen.writeArrayFieldStart("users");
				for (User user : users) {
					jsonGen.writeObject(user);
				}
				
				jsonGen.writeEndArray();
				jsonGen.writeEndObject();
				jsonGen.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		finally {
			writeLock.unlock();
		}
		
	}
	
	private Iterator<JsonNode> getUsersNodeIteratorFromJsonFile() {
		readLock.lock();
		try {
			final JsonFactory factory = new JsonFactory();
			try {
				JsonParser jp = factory.createParser(new FileInputStream("src/main/resources/users.json"));
				jp.setCodec(new ObjectMapper());
				JsonNode node = jp.readValueAsTree();
				JsonNode nodeUsers = node.get("users");
				Iterator<JsonNode> itr =  nodeUsers.elements();
				return itr;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return EmptyIterator.instance();
		}
		finally {
			readLock.unlock();
		}
	}
	
	private User readUser(final JsonNode element) {
		User user = new User();
		
		JsonNode temp = element.get("id");
		user.setId(temp.asText());
		
		temp = element.get("firstName");
		user.setFirstName(temp.asText());
		
		temp = element.get("lastName");
		user.setLastName(temp.asText());
		
		return user;
	}

	@Override
	public List<User> findAll() {
		List<User> users = new LinkedList<User>();
		Iterator<JsonNode> itr = getUsersNodeIteratorFromJsonFile();
		JsonNode element;
		User user;
		while (itr.hasNext()) {
			element = itr.next();
			user = readUser(element);
			users.add(user);
		}
		return users;
	}
	

	@Override
	public User findById(String id) {
		Iterator<JsonNode> itr = getUsersNodeIteratorFromJsonFile();
		JsonNode element;
		User user;
		while (itr.hasNext()) {
			element = itr.next();
			user = readUser(element);
			if (user.getId().equals(id)) return user;
		}
		return null;
	}

	@Override
	public void remove(User user) {
		Iterator<JsonNode> itr = getUsersNodeIteratorFromJsonFile();
		List<User> users = new LinkedList<>();
		JsonNode element;
		User temp;
		while (itr.hasNext()) {
			element = itr.next();
			temp = readUser(element);
			if (!temp.getId().equals(user.getId())) 
				users.add(temp);
		}
		writeDataInJsonFile(users);
	}

	@Override
	public User update(final User user) {
		Iterator<JsonNode> itr = getUsersNodeIteratorFromJsonFile();
		List<User> users = new LinkedList<>();
		JsonNode element;
		User temp;
		while (itr.hasNext()) {
			element = itr.next();
			temp = readUser(element);
			if (temp.getId().equals(user.getId())) { 
				temp.setId(user.getId());
				temp.setFirstName(user.getFirstName());
				temp.setLastName(user.getLastName());
			}
			users.add(temp);
		}
		writeDataInJsonFile(users);
		return user;
	}

	@Override
	public User save(final User user) {
		List<User> users = findAll();
		users.add(user);
		writeDataInJsonFile(users);
		return user;
	}
}
