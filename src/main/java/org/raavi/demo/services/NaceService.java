package org.raavi.demo.services;

import org.raavi.demo.data.NaceData;
import org.raavi.demo.dao.NaceDAO;
import org.raavi.demo.repository.NaceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NaceService {
    @Autowired
    private final NaceRepo naceRepo;

    public NaceService(NaceRepo naceRepo) {
        this.naceRepo = naceRepo;
    }

    public List<NaceData> findAll() {
        return naceRepo.findAll().stream()
                .map(NaceData::fromEntity)
                .collect(Collectors.toList());
    }

    public NaceData findByOrder(int order) {
        NaceDAO entity = naceRepo.findByOrder(order);
        if (entity == null) {
            return null;
        }
        return NaceData.fromEntity(entity);
    }

    public NaceData insertEntity(NaceData naceData) {
        NaceDAO oldEntity = naceData.getOrder() != null ? naceRepo.findByOrder(naceData.getOrder()) : null;
        if (oldEntity != null) {
            return null;
        }
        return NaceData.fromEntity(naceRepo.save(naceData.toEntity()));
    }

    public NaceData updateEntity(int order, NaceData naceData) {
        NaceDAO oldEntity = naceRepo.findByOrder(order);
        if (oldEntity == null) {
            return null;
        }
        return NaceData.fromEntity(naceRepo.save(naceData.toEntity()));
    }

    public List<NaceData> bulkInsert(List<NaceData> naceData) {
        List<NaceDAO> entities = naceData.stream()
                .map(NaceData::toEntity)
                .collect(Collectors.toList());
        return naceRepo.saveAll(entities).stream()
                .map(NaceData::fromEntity)
                .collect(Collectors.toList());
    }

    public boolean deleteEntity(int order) {
        NaceDAO oldEntity = naceRepo.findByOrder(order);
        if (oldEntity == null) {
            return false;
        }
        naceRepo.delete(oldEntity);
        return true;
    }
}
