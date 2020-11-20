package com.byka.germanstandard.service;

import com.byka.germanstandard.exception.DataLoadException;
import com.byka.germanstandard.repository.AdsMetricRepository;
import com.byka.germanstandard.model.AdsMetric;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class FileLoadService {
    @Autowired
    private AdsMetricRepository adsMetricRepository;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

    public void load(InputStream dataStream) throws IOException {
        try (dataStream) {
            List<AdsMetric> ads = getDataToLoad(dataStream);
            adsMetricRepository.saveAll(ads);
        }
    }

    private List<AdsMetric> getDataToLoad(InputStream dataStream) {
        List<AdsMetric> dataToAdd = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(dataStream))) {
            String[] headers = csvReader.readNext();
            String[] line = null;
            while ((line = csvReader.readNext()) != null) {
                dataToAdd.add(transformLine(line));
            }
        } catch (IOException | CsvValidationException | ParseException e) {
            log.warn("Cannot load data because of exception", e);
            throw new DataLoadException(e);
        }

        return dataToAdd;
    }

    private AdsMetric transformLine(String[] line) throws ParseException {
        if (line.length != 5) {
            throw new DataLoadException("Incorrect csv format in line: " +  Arrays.toString(line));
        }

        AdsMetric adsMetric = new AdsMetric();
        adsMetric.setDatasource(line[0]);
        adsMetric.setCampaign(line[1]);
        adsMetric.setDate(dateFormat.parse(line[2]));
        adsMetric.setClicks(Long.parseLong(line[3]));
        adsMetric.setImpressions(Long.parseLong(line[4]));
        return adsMetric;
    }
}
