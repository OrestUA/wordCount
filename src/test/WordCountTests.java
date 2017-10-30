package test;

import main.WordCount;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

/**
 * Created by YSkakun on 10/29/2017.
 */
public class WordCountTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static Map<String, Long> expectedMap;

    private static List<String> expectedSortedList;

    @BeforeClass
    public static void setUp() {
        expectedMap = new HashMap<>();
        expectedMap.put("all", 2L);
        expectedMap.put("the", 2L);
        expectedMap.put("how", 2L);
        expectedMap.put("data", 1L);
        expectedMap.put("bells", 3L);
        expectedMap.put("bid", 1L);
        expectedSortedList = Arrays.asList("bells=3","all=2","how=2","the=2","bid=1");
    }

    @Test
    public void noSpecifiedCommandLineArgumentsThrowsExceptionTest() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Use program: wordCount <filePath> <optional_word_limit>");
        String[] arr = new String[0];
        WordCount.main(arr);
    }

    @Test
    public void countWordsFrequenciesTest() throws IOException {
        Path path = Paths.get("test.txt");
        Map<String, Long> actualMap = WordCount.countWordsFrequencies(path);
        Assert.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void sortWordFrequenciesTest() {
        final Comparator<Map.Entry<String, Long>> countComparator = comparing(Map.Entry::getValue, reverseOrder());
        final Comparator<Map.Entry<String, Long>> alphabetComparator = comparing(Map.Entry::getKey);
        int limit = 5;
        List<String> actualSortedList = WordCount.sortWordFrequencies(expectedMap, countComparator, alphabetComparator, limit);
        Assert.assertEquals(expectedSortedList,actualSortedList);
    }
}