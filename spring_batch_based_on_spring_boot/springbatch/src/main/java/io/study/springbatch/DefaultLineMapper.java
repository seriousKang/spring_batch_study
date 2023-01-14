package io.study.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;

@RequiredArgsConstructor
public class DefaultLineMapper<T> implements LineMapper<T> {
    private final LineTokenizer lineTokenizer;
    private final FieldSetMapper<T> fieldSetMapper;

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        return fieldSetMapper.mapFieldSet(lineTokenizer.tokenize(line));
    }
}
