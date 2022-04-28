package org.raavi.demo.services;

import io.vavr.collection.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raavi.demo.dao.NaceDAO;
import org.raavi.demo.data.NaceData;
import org.raavi.demo.repository.NaceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
public class NaceServiceTest {
    @Autowired
    private NaceRepo naceRepo;

    @Autowired
    private NaceService naceService;

    @BeforeEach
    public void initializeData() {
        naceRepo.deleteAll();
        List<NaceData> testCases = List.of(
                new NaceData(1, 1, "1.1", "", "Desc 1", "Includes 1", "Also Includes 1", "Rulings 1", "Excludes 1", "Reference 1"),
                new NaceData(2, 2, "2.1", "1.1", "Desc 2", "Includes 2", "Also Includes 2", "Rulings 2", "Excludes 2", "Reference 2"),
                new NaceData(3, 3, "3.1", "1.1", "Desc 3", "Includes 3", "Also Includes 3", "Rulings 3", "Excludes 3", "Reference 3"),
                new NaceData(4, 4, "4.1", "1.1", "Desc 4", "Includes 4", "Also Includes 4", "Rulings 4", "Excludes 4", "Reference 4"),
                new NaceData(5, 3, "3.2", "3.1", "Desc 5", "Includes 5", "Also Includes 5", "Rulings 5", "Excludes 5", "Reference 5")
        );
        naceRepo.saveAll(testCases.map(NaceData::toEntity).toJavaList());
    }

    @Test
    public void shouldGetFiveObjects() {
        List<NaceData> result = List.ofAll(naceService.findAll());

        assertEquals(5, result.size());
    }

    @Test
    public void shouldGetOneObject() {
        List<NaceDAO> naceList = List.ofAll(naceRepo.findAll());
        NaceData result = naceService.findByOrder(naceList.head().getOrder());

        assertEquals(naceList.head().getExcludes(), result.getExcludes());
    }

    @Test
    public void shouldInsertOneEntity() {
        NaceData result = naceService.insertEntity(new NaceData(null, 3, "7.1", "1.1", "Desc x", "Includes x", "Also Includes x", "Rulings x", "Excludes x", "Reference x"));

        assertEquals("Excludes x", naceRepo.findByOrder(result.getOrder()).getExcludes());
        assertEquals(6, naceRepo.findAll().size());
    }

    @Test
    public void shouldFailToInsertEntity() {
        List<NaceDAO> naceList = List.ofAll(naceRepo.findAll());
        NaceData result = naceService.insertEntity(new NaceData(naceList.head().getOrder(), 3, "7.1", "1.1", "Desc x", "Includes x", "Also Includes x", "Rulings x", "Excludes x", "Reference x"));

        assertNull(result);
        assertEquals(5, naceRepo.findAll().size());
    }
}
