package com.dregronprogram.json_parse;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

import com.dregronprogram.json_parse.JsonParser;
import com.dregronprogram.tiled_map.Tiled;

public class JsonParserTest {

	private JsonParser testee;
	
	private InputStream inputStram = JsonParserTest.class.getResourceAsStream("../levels/jsonParseTest.json");
	
	@Test
	public void jsonParseTest() throws Exception {
		
		testee = new JsonParser();
		Tiled tiled = testee.readValue(inputStram, Tiled.class);
		assertNotNull(tiled);
	}
	
	@Test
	public void jsonParseAssertIntegerValues() throws Exception {
		
		testee = new JsonParser();
		Tiled tiled = testee.readValue(inputStram, Tiled.class);
		assertEquals(25, tiled.getWidth());
		assertEquals(10, tiled.getHeight());
		assertEquals(32, tiled.getTileWidth());
		assertEquals(30, tiled.getTileHeight());
		assertNotNull(tiled.getLayers());
		assertNotNull(tiled.getTilesets());
	}
}
