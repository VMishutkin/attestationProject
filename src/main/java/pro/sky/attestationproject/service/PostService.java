package pro.sky.attestationproject.service;

import org.springframework.stereotype.Service;
import pro.sky.attestationproject.repository.PackageRepository;
@Service
public class PostService {
private final PackageRepository packageRepository;


    public PostService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }



}
