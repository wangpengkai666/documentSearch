package com.example.documentseach.common;

import com.example.documentseach.common.util.GetJSONFromTxt;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class GetJSONFromTxtTest {
    @Test
    public void readDslFileInJarFileTest() throws FileNotFoundException {
        String fileName = "article";
        System.out.println(GetJSONFromTxt.readDslFileInJarFile(fileName));
    }
}
