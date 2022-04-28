package org.raavi.demo.controller;

import org.raavi.demo.data.NaceData;
import org.raavi.demo.services.NaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NaceController {
    @Autowired
    NaceService naceService;

    @RequestMapping(value = "/nace/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NaceData> findAll() {
        return naceService.findAll();
    }

    @RequestMapping(value = "/nace/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NaceData> findByOrder(@PathVariable int order) {
        NaceData result = naceService.findByOrder(order);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/nace/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NaceData> insertEntity(@RequestBody NaceData entry) {
        NaceData result = naceService.insertEntity(entry);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @RequestMapping(value = "/nace/{order}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NaceData> updateEntity(@PathVariable int order, @RequestBody NaceData entry) {
        NaceData result = naceService.updateEntity(order, entry);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/nace/{order}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NaceData> deleteEntity(@PathVariable int order) {
        boolean result = naceService.deleteEntity(order);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/nace/bulk-import", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NaceData> insertEntity(@RequestBody List<NaceData> entries) {
        return naceService.bulkInsert(entries);
    }
}
