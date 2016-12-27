package com.mpay24.payment.mapper;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SdkApiObjectMapperTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetExpiredateAsLong() throws ParseException {
		assertEquals(new Long(1604), new SdkApiObjectMapper().getExpiredateAsLong(getDate("04/2016")));
	}

	private Date getDate(String dateString) throws ParseException {
		return new SimpleDateFormat("MM/yyyy").parse(dateString);
	}

}
