/*
 * Copyright (c) 2024 Bear Giles <bgiles@coyotesong.com>.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coyotesong.dojo.youtube.repository.poi;

import com.coyotesong.dojo.youtube.model.Channel;
import com.coyotesong.dojo.youtube.model.WikipediaTopic;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * Workbook containing 'channel' information
 */
public class ChannelWorkbook extends XSSFWorkbook {
    private final File file;
    private final CellStyle headerStyle;
    private final CellStyle booleanHeaderStyle;
    private final CellStyle rowStyle;
    private final CellStyle booleanRowStyle;
    private final CellStyle descriptionStyle;
    private final CellStyle creationDateStyle;
    private final CellStyle countryStyle;
    private final CellStyle langStyle;
    private final Font descriptionFont;

    public ChannelWorkbook(File file) {
        this.file = file;


        short lightGray = IndexedColors.GREY_40_PERCENT.getIndex();
        this.headerStyle = super.createCellStyle();

        final Font originalFont = getFontAt(this.headerStyle.getFontIndex());
        final Font headerFont = createFont();
        headerFont.setFontName(originalFont.getFontName());
        headerFont.setCharSet(originalFont.getCharSet());
        headerFont.setFontHeightInPoints(originalFont.getFontHeightInPoints());
        headerFont.setBold(true);
        this.headerStyle.setFont(headerFont);

        this.booleanHeaderStyle = super.createCellStyle();
        this.booleanHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        this.booleanHeaderStyle.setFont(headerFont);

        this.rowStyle = super.createCellStyle();

        this.booleanRowStyle = super.createCellStyle();
        this.booleanRowStyle.setAlignment(HorizontalAlignment.CENTER);

        // This column may be easier to read if we enable wrapText but
        // that throws off the calculation of the required row height and
        // that usually results in oddly truncated text. It seems better
        // to have the correct height and leave it to the user to decide
        // whether to adjust the width.
        //
        // We can't use sheet.autoSizeColumn() since there will always
        // be some description that's 1000 characters long without any
        // line breaks. Too bad there's no way to specify lower and upper
        // limits during the autosize calculations, or a reliable way
        // to specify a properly autosized row. (Using -1 works with
        // MS Excel but not LibreOffice.)
        this.descriptionStyle = super.createCellStyle();
        // this.descriptionStyle.setWrapText(true);

        this.creationDateStyle = super.createCellStyle();
        this.creationDateStyle.setAlignment(HorizontalAlignment.CENTER);

        this.countryStyle = super.createCellStyle();
        this.countryStyle.setAlignment(HorizontalAlignment.CENTER);

        this.langStyle = super.createCellStyle();
        this.langStyle.setAlignment(HorizontalAlignment.CENTER);

        this.descriptionFont = getFontAt(this.descriptionStyle.getFontIndex());

        // add borders, padding, etc.
        final List<CellStyle> styles = Arrays.asList(headerStyle, booleanHeaderStyle,
                rowStyle, booleanRowStyle, descriptionStyle, creationDateStyle, countryStyle, langStyle);
        for (CellStyle style : styles) {
            style.setVerticalAlignment(VerticalAlignment.TOP);
            style.setShrinkToFit(false);

            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);

            style.setTopBorderColor(lightGray);
            style.setBottomBorderColor(lightGray);
            style.setLeftBorderColor(lightGray);
            style.setRightBorderColor(lightGray);
        }
    }

    /**
     * Close workbook and write it to disk
     *
     * @throws IOException the disk operation failed
     */
    public void close() throws IOException {
        try (OutputStream os = new FileOutputStream(file)) {
            super.write(os);
        }
        super.close();
    }

    /**
     * List topics present in collection of Channels
     *
     * @param channels list of Channels
     * @return list of topics
     */
    List<WikipediaTopic> listUniqueTopics(Collection<Channel> channels) {
        final Map<URL, WikipediaTopic> topics = new HashMap<>();
        channels.stream().map(Channel::getTopicCategories).forEach(s -> {
            for (WikipediaTopic topic : s) {
                topics.put(topic.getUrl(), topic);
            }
        });

        final List<WikipediaTopic> sortedTopics = new ArrayList<>(topics.values());
        Collections.sort(sortedTopics, (s, p) -> s.getLabel().compareTo(p.getLabel()));

        return sortedTopics;
    }

    /**
     * Add a collection of Channels to the workbook.
     *
     * @param channels list of Channels
     */
    public void addChannels(Collection<Channel> channels) {
        final List<WikipediaTopic> topics = listUniqueTopics(channels);

        // add list of topics
        addTopicsSheet(topics);

        // add list of all channels
        final List<Channel> sorted = new ArrayList<>(channels);
        Collections.sort(sorted, (s, p) -> s.getTitle().compareTo(p.getTitle()));
        addChannelsSheet("channels", sorted, (c) -> true, (t) -> true);

        // add list of channels for each topic.
        for (WikipediaTopic topic : topics) {
            final Predicate<Channel> channelPredicate = c -> c.getTopicCategories().contains(topic);
            final Predicate<WikipediaTopic> topicPredicate = t -> !t.equals(topic);
            addChannelsSheet(topic.getLabel(), sorted, channelPredicate, topicPredicate);
        }
    }

    /**
     * Add list of topics
     *
     * @param topics
     */
    void addTopicsSheet(Collection<WikipediaTopic> topics) {
        final Sheet sheet = createSheet("topics");

        // add header row
        addTopicHeaders(sheet.createRow(0));

        // add formatting
        sheet.setDefaultColumnStyle(0, rowStyle);
        sheet.setDefaultColumnStyle(1, booleanRowStyle);

        // add individual rows
        int rowno = 1;
        for (WikipediaTopic topic : topics) {
            final Hyperlink hyperlink = getCreationHelper().createHyperlink(HyperlinkType.URL);
            hyperlink.setAddress(topic.getUrl().toExternalForm());
            hyperlink.setLabel(topic.getLabel());

            final Row row = sheet.createRow(rowno++);
            row.createCell(0).setCellValue(hyperlink.getLabel());
            row.getCell(0).setHyperlink(hyperlink);
            row.createCell(1).setCellValue(topic.isCustom());
        }

        final CellRangeAddress region = new CellRangeAddress(0, sheet.getLastRowNum(), 0, 10);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, sheet);

        final int black = IndexedColors.BLACK.getIndex();
        RegionUtil.setTopBorderColor(black, region, sheet);
        RegionUtil.setBottomBorderColor(black, region, sheet);
        RegionUtil.setLeftBorderColor(black, region, sheet);
        RegionUtil.setRightBorderColor(black, region, sheet);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
    }

    /**
     * Add topic headers
     */
    void addTopicHeaders(Row row) {
        row.createCell(0).setCellValue("Label");
        row.createCell(1).setCellValue("Custom");

        // add formatting
        row.getCell(0).setCellStyle(headerStyle);
        row.getCell(1).setCellStyle(booleanHeaderStyle);
    }

    /**
     * Add list of channels
     *
     * @param name
     * @param channels
     */
    void addChannelsSheet(String name, Collection<Channel> channels,
                          Predicate<Channel> channelPredicate, Predicate<WikipediaTopic> topicPredicate) {

        // select subset of channels
        final Collection<Channel> subset = channels.stream().filter(channelPredicate).toList();

        final Sheet sheet = createSheet(name);
        sheet.getPrintSetup().setLandscape(true);
        addChannelHeaders(sheet.createRow(0));

        // set column formats
        for (int i = 0; i <= 10; i++) {
            sheet.setDefaultColumnStyle(i, rowStyle);
        }
        sheet.setDefaultColumnStyle(1, descriptionStyle);
        sheet.setDefaultColumnStyle(2, creationDateStyle);
        sheet.setDefaultColumnStyle(6, countryStyle);
        sheet.setDefaultColumnStyle(7, langStyle);

        // add list of channels
        int rowno = 1;
        for (Channel channel : subset) {
            addChannel(sheet.createRow(rowno++), channel, topicPredicate);
        }

        final CellRangeAddress region = new CellRangeAddress(0, sheet.getLastRowNum(), 0, 10);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, sheet);

        final int black = IndexedColors.BLACK.getIndex();
        RegionUtil.setTopBorderColor(black, region, sheet);
        RegionUtil.setBottomBorderColor(black, region, sheet);
        RegionUtil.setLeftBorderColor(black, region, sheet);
        RegionUtil.setRightBorderColor(black, region, sheet);

        // sheet.autoSizeColumn(0);
        // int width = sheet.getColumnWidth(0);
        // LOG.info("width: {}", width);
        int width = 6000;
        sheet.setColumnWidth(0, width);
        sheet.setColumnWidth(1, 3 * width);
        for (int i = 2; i <= 10; i++) {
            sheet.autoSizeColumn(i);
        }

        sheet.setColumnHidden(5, true);
        sheet.setColumnHidden(9, true);
        sheet.setColumnHidden(10, true);

        sheet.createFreezePane(1, 1);
    }

    /**
     * Add 'channel' headers
     *
     * @param row
     */
    void addChannelHeaders(Row row) {
        row.createCell(0).setCellValue("Title");
        row.createCell(1).setCellValue("Description");
        row.createCell(2).setCellValue("Creation Date");
        row.createCell(3).setCellValue("Videos");
        row.createCell(4).setCellValue("Subscribers");
        row.createCell(5).setCellValue("Views");
        row.createCell(6).setCellValue("Country");
        row.createCell(7).setCellValue("Lang");
        row.createCell(8).setCellValue("Topics");
        row.createCell(9).setCellValue("Handle");
        row.createCell(10).setCellValue("Channel Id");

        // add formatting
        for (int i = 0; i <= 10; i++) {
            row.getCell(i).setCellStyle(headerStyle);
        }
    }

    /**
     * Add individual channel
     *
     * @param row
     * @param channel
     * @param predicate
     */
    void addChannel(Row row, Channel channel, Predicate<WikipediaTopic> predicate) {
        final Hyperlink hyperlink = getCreationHelper().createHyperlink(HyperlinkType.URL);
        hyperlink.setAddress("https://youtube.com/" + channel.getChannelId());
        hyperlink.setLabel(channel.getTitle());

        row.createCell(0).setCellValue(hyperlink.getLabel());
        row.getCell(0).setHyperlink(hyperlink);
        row.createCell(1);
        if (channel.getDescription() != null) {
            final String description = channel.getDescription().trim();
            row.getCell(1).setCellValue(description);
            int lines = description.split("\n").length;
            row.setHeightInPoints(4 + lines * descriptionFont.getFontHeightInPoints());
        }
        row.createCell(2);
        if (channel.getPublishedAt() != null) {
            // FIXME - use actual date format
            row.getCell(2).setCellValue(ISO_INSTANT.format(channel.getPublishedAt()).substring(0, 10));
        }
        row.createCell(3).setCellValue(channel.getVideoCount());
        row.createCell(4).setCellValue(channel.getSubscriberCount());
        row.createCell(5).setCellValue(channel.getViewCount());
        row.createCell(6).setCellValue(channel.getCountry());
        row.createCell(7).setCellValue(channel.getLang());
        row.createCell(8).setCellValue(channel.getTopicCategories()
                .stream()
                .filter(predicate)
                .map(WikipediaTopic::getLabel)
                .distinct()
                .sorted()
                .collect(Collectors.joining(", ")));
        row.createCell(9).setCellValue(channel.getHandle());
        row.createCell(10).setCellValue(channel.getChannelId());
    }
}
