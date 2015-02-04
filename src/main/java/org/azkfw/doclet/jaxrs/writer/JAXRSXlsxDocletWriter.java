/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.doclet.jaxrs.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.azkfw.doclet.DocletParserException;
import org.azkfw.doclet.jaxrs.model.APIModel;
import org.azkfw.doclet.jaxrs.model.AreasModel;
import org.azkfw.doclet.jaxrs.parser.JAXRSDocletParser;
import org.azkfw.doclet.jaxrs.parser.JAXRSDocletParserEvent;
import org.azkfw.doclet.jaxrs.parser.JAXRSDocletParserListener;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;

/**
 * @since 1.0.0
 * @version 1.0.0 2015/02/03
 * @author kawakicchi
 */
public class JAXRSXlsxDocletWriter {

	private JAXRSDocletParser parser;

	Map<String, APIModel> apis;

	public JAXRSXlsxDocletWriter(final JAXRSDocletParser parser) {
		this.parser = parser;

		apis = new HashMap<String, APIModel>();
	}

	public void write(final RootDoc root) {
		try {

			parser.addListener(new JAXRSDocletParserListener() {
				@Override
				public void jaxrsDocletParserFindMethodDoc(JAXRSDocletParserEvent event, ClassDoc classDoc, MethodDoc methodDoc) {
				}

				@Override
				public void jaxrsDocletParserFindClassDoc(JAXRSDocletParserEvent event, ClassDoc classDoc) {
				}

				@Override
				public void jaxrsDocletParserFindAreas(JAXRSDocletParserEvent event, AreasModel model) {

				}

				@Override
				public void jaxrsDocletParserFindAPI(final JAXRSDocletParserEvent event, final APIModel model) {
					addApi(model);
				}
			});

			parser.parse(root);

			List<APIModel> bufApis = new ArrayList<>();
			for (String key : apis.keySet()) {
				bufApis.add(apis.get(key));
			}
			Collections.sort(bufApis, new Comparator<APIModel>() {
				@Override
				public int compare(APIModel o1, APIModel o2) {
					String s1 = o1.getPath();
					String s2 = o2.getPath();
					if (null == s1 && null == s2) {
						return 0;
					} else if (null == s2) {
						return 1;
					} else if (null == s1) {
						return -1;
					} else {
						return s1.compareTo(s2);
					}
				}
			});

			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheetAPIList = wb.createSheet("IF一覧");
			printSheet(wb, sheetAPIList, bufApis);

			for (APIModel api : bufApis) {
				if (null != api.getId() && 0 < api.getId().length()) {
					XSSFSheet sheetApi = wb.createSheet(api.getId());
					printSheet(wb, sheetApi, api);
				}
			}

			FileOutputStream out = new FileOutputStream(new File("sample.xlsx"));
			wb.write(out);
			out.close();

		} catch (DocletParserException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void printSheet(final XSSFWorkbook wb, final XSSFSheet sheet, final APIModel api) {
		
		////////////////////////////////////////////////////
		XSSFRow row = null;
		XSSFCell cell = null;
		
		////////////////////////////////////////////////////

		
		row = sheet.createRow(1); //////////////////////////
		cell = row.createCell(1, Cell.CELL_TYPE_STRING);
		cell.setCellValue("機能名");
		
		cell = row.createCell(5, Cell.CELL_TYPE_STRING);
		cell.setCellValue(s(api.getName()));
		
		cell = row.createCell(22, Cell.CELL_TYPE_STRING);
		cell.setCellValue("文字コード");
		
		cell = row.createCell(25, Cell.CELL_TYPE_STRING);
		cell.setCellValue("TUF-8");
		
		row = sheet.createRow(2); //////////////////////////
		cell = row.createCell(1, Cell.CELL_TYPE_STRING);
		cell.setCellValue("URL");		
		cell = row.createCell(5, Cell.CELL_TYPE_STRING);
		cell.setCellValue(s(api.getPath()));

		cell = row.createCell(15, Cell.CELL_TYPE_STRING);
		cell.setCellValue("メソッド");
		cell = row.createCell(18, Cell.CELL_TYPE_STRING);
		cell.setCellValue("");
	}
	private void printSheet(final XSSFWorkbook wb, final XSSFSheet sheet, final List<APIModel> apis) {

		XSSFCell cell = null;
		XSSFRow row = null;

		////////////////////////////////////////////////////
		XSSFFont fontBold = wb.createFont();
		fontBold.setBold(true);

		CellStyle styleHeader = wb.createCellStyle();
		styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleHeader.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		styleHeader.setFont(fontBold);
		styleHeader.setBorderTop(CellStyle.BORDER_THIN);
		styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
		styleHeader.setBorderRight(CellStyle.BORDER_THIN);
		styleHeader.setBorderBottom(CellStyle.BORDER_DOUBLE);

		row = sheet.createRow(0); //////////////////////////
		cell = row.createCell(1, Cell.CELL_TYPE_STRING);

		CellStyle style1 = wb.createCellStyle();
		style1.setFont(fontBold);
		cell.setCellStyle(style1);
		cell.setCellValue("外部I/F (API) 一覧");

		row = sheet.createRow(1); //////////////////////////
		cell = row.createCell(1, Cell.CELL_TYPE_STRING);
		cell.setCellValue("APIの一覧を下記の通り定義する");

		row = sheet.createRow(4); //////////////////////////
		cell = row.createCell(1, Cell.CELL_TYPE_STRING);
		cell.setCellValue("ID");
		cell.setCellStyle(styleHeader);
		cell = row.createCell(2, Cell.CELL_TYPE_STRING);
		cell.setCellValue("Group");
		cell.setCellStyle(styleHeader);
		cell = row.createCell(3, Cell.CELL_TYPE_STRING);
		cell.setCellValue("Name");
		cell.setCellStyle(styleHeader);
		cell = row.createCell(4, Cell.CELL_TYPE_STRING);
		cell.setCellValue("Comment");
		cell.setCellStyle(styleHeader);

		int offsetRow = 5;
		int offsetCol = 1;
		for (int i = 0; i < apis.size(); i++) {
			APIModel api = apis.get(i);

			row = sheet.createRow(offsetRow + i);

			cell = row.createCell(offsetCol + 0, Cell.CELL_TYPE_STRING);
			cell.setCellValue(s(api.getId()));

			cell = row.createCell(offsetCol + 2, Cell.CELL_TYPE_STRING);
			cell.setCellValue(s(api.getName()));

			cell = row.createCell(offsetCol + 3, Cell.CELL_TYPE_STRING);
			cell.setCellValue(s(api.getComment()));
		}
	}

	private void addApi(final APIModel api) {
		if (!apis.containsKey(api.getPath())) {
			apis.put(api.getPath(), api);
		}
	}

	private static String s(final String string) {
		if (null != string) {
			return string;
		}
		return "";
	}
}
