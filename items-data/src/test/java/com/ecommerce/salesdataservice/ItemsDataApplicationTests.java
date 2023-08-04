package com.ecommerce.salesdataservice;

import com.ecommerce.itemsdata.ItemsDataApplication;
import com.ecommerce.itemsdata.controller.ItemController;
import com.ecommerce.itemsdata.model.Item;
import com.ecommerce.itemsdata.service.ItemService;
import com.ecommerce.itemsdata.util.StringParser;
import com.ecommerce.itemsdata.util.dev.ItemGenerator;
import org.apache.commons.lang.math.DoubleRange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ItemsDataApplication.class})
class ItemsDataApplicationTests {

	ItemController itemController;
	ItemService itemService;

	ItemGenerator itemGenerator;

	@Autowired
	public ItemsDataApplicationTests(ItemController itemController, ItemService itemService, ItemGenerator itemGenerator) {
		this.itemController = itemController;
		this.itemService = itemService;
		this.itemGenerator = itemGenerator;
	}

	@Test
	void test() {
		DoubleRange dr = new DoubleRange(30.0, 100.0);
		System.out.println(dr.containsDouble(45.0));
	}

	@Test
	void generateItem(){
		Item item = itemGenerator.generate();
		System.out.println(item);
	}






}
