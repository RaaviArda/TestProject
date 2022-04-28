package org.raavi.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.collection.List;
import io.vavr.jackson.datatype.VavrModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raavi.demo.dao.NaceDAO;
import org.raavi.demo.data.NaceData;
import org.raavi.demo.repository.NaceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NaceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NaceRepo naceRepo;

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
    public void shouldGetOKAndFiveObjects() throws Exception {
        MvcResult result = mockMvc.perform(get("/nace/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<NaceData> naceData = getNaceDataAsList(result.getResponse().getContentAsString());
        assertEquals(5, naceData.size());
    }

    @Test
    public void shouldGetOKAndOneObject() throws Exception {
        List<NaceDAO> naceList = List.ofAll(naceRepo.findAll());
        MvcResult result = mockMvc.perform(get(String.format("/nace/%s/", naceList.head().getOrder())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        NaceData naceData = getNaceDataAsSingleObject(result.getResponse().getContentAsString());
        assertNotNull(naceData);
    }

    @Test
    public void shouldGet404Error() throws Exception {
        mockMvc.perform(get("/nace/7/"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldInsertOneObject() throws Exception {
        NaceData newEntry = new NaceData(6, 3, "3.3", "3.1", "Desc 6",
                "Includes 6", "Also Includes 6", "Rulings 6", "Excludes 6", "Reference 6");
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(post("/nace/").contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newEntry)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        NaceData naceData = getNaceDataAsSingleObject(result.getResponse().getContentAsString());
        assertNotNull(naceData);
        assertNotNull(naceRepo.findByOrder(naceData.getOrder()));
    }

    @Test
    public void shouldUpdateOneObject() throws Exception {
        List<NaceDAO> naceList = List.ofAll(naceRepo.findAll());
        NaceData newEntry = NaceData.fromEntity(naceList.head());
        newEntry.setExcludes("New Exclude");
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(post(String.format("/nace/%s/", newEntry.getOrder())).contentType(APPLICATION_JSON)
                    .content(mapper.writeValueAsString(newEntry)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        NaceData naceData = getNaceDataAsSingleObject(result.getResponse().getContentAsString());
        assertEquals("New Exclude", naceData.getExcludes());
        assertEquals("New Exclude", naceRepo.findByOrder(naceData.getOrder()).getExcludes());
    }

    @Test
    public void shouldGet404WhileUpdating() throws Exception {
        List<NaceDAO> naceList = List.ofAll(naceRepo.findAll());
        NaceData newEntry = NaceData.fromEntity(naceList.head());
        String oldExcludes = newEntry.getExcludes();
        newEntry.setExcludes("New Exclude");
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/nace/1528269713/").contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newEntry)))
                .andDo(print())
                .andExpect(status().isNotFound());

        assertEquals(oldExcludes, naceRepo.findByOrder(newEntry.getOrder()).getExcludes());
    }

    @Test
    public void shouldDeleteOneObject() throws Exception {
        List<NaceDAO> naceList = List.ofAll(naceRepo.findAll());
        NaceData newEntry = NaceData.fromEntity(naceList.head());
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(delete(String.format("/nace/%s/", newEntry.getOrder())).contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newEntry)))
                .andDo(print())
                .andExpect(status().isOk());

        List<NaceDAO> newNaceList = List.ofAll(naceRepo.findAll());
        assertEquals(4, newNaceList.size());
        assertFalse(newNaceList.contains(naceList.head()));
    }

    @Test
    public void shouldGet404WhileDeleting() throws Exception {
        List<NaceDAO> naceList = List.ofAll(naceRepo.findAll());
        NaceData newEntry = NaceData.fromEntity(naceList.head());
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(delete("/nace/1528269713/").contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newEntry)))
                .andDo(print())
                .andExpect(status().isNotFound());

        List<NaceDAO> newNaceList = List.ofAll(naceRepo.findAll());
        assertEquals(5, newNaceList.size());
        assertTrue(newNaceList.contains(naceList.head()));
    }


    private List<NaceData> getNaceDataAsList(String content) throws com.fasterxml.jackson.core.JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new VavrModule());
        return mapper.readValue(content, new TypeReference<List<NaceData>>() {
        });
    }

    private NaceData getNaceDataAsSingleObject(String content) throws com.fasterxml.jackson.core.JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new VavrModule());
        return mapper.readValue(content, new TypeReference<NaceData>() {
        });
    }
}
