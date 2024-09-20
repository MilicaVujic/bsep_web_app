package com.example.security.service;

import com.example.security.model.Advertisement;
import com.example.security.model.AdvertisementRequest;
import com.example.security.model.Client;
import com.example.security.model.RequestStatus;
import com.example.security.repository.AdvertisementRepository;
import com.example.security.repository.AdvertisementRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class AdvertisementService implements  IAdvertisementService{
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private AdvertisementRequestRepository advertisementRequestRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AdvertisementRequestService advertisementRequestService;
    Logger logger= LoggerFactory.getLogger(AdvertisementService.class);


    @Override
    public ArrayList<Advertisement> getAll() {
        ArrayList<Advertisement> advertisements = new ArrayList<>();
        try {
            logger.info("Fetching all advertisements...");
            advertisements = (ArrayList<Advertisement>) advertisementRepository.findAll();
            logger.info("Advertisements fetched successfully.");
        } catch (Exception e) {
            logger.error("An error occurred while fetching advertisements: {}", e.getMessage(), e);
        }
        return advertisements;
    }



    @Override
    public Advertisement save(Advertisement advertisement, Long id, Long requestId) {
        try {
            logger.info("Saving advertisement...");
            AdvertisementRequest request = advertisementRequestService.getById(requestId);
            advertisement.setClient(request.getClient());
            advertisement.setRequest(request);

            if (new Date(advertisement.getRequest().getExpirationTime().getTime()).before(new Date())) {
                request.setRequestStatus(RequestStatus.REJECTED);
                advertisementRequestRepository.save(request);
                return null;
            }

            advertisementRequestService.update(request);
            Advertisement savedAdvertisement = advertisementRepository.save(advertisement);
            logger.info("Advertisement saved successfully.");
            return savedAdvertisement;
        } catch (Exception e) {
            logger.error("An error occurred while saving advertisement: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ArrayList<Advertisement> findAllByClientId(Long id) {
        try {
            logger.info("Finding all advertisements by client ID: {}", id);
            ArrayList<Advertisement> advertisements = advertisementRepository.findAllByClientId(id);
            logger.info("Found {} advertisements for client ID: {}", advertisements.size(), id);
            return advertisements;
        } catch (Exception e) {
            logger.error("An error occurred while finding advertisements by client ID {}: {}", id, e.getMessage(), e);
            return null; // ili možete ponovo baciti izuzetak ako je to prikladnije za vašu aplikaciju
        }
    }


    public void deleteAll(ArrayList<Advertisement> advertisements) {
        try {
            logger.info("Deleting all advertisements...");
            advertisementRepository.deleteAll(advertisements);
            logger.info("All advertisements deleted successfully.");
        } catch (Exception e) {
            logger.error("An error occurred while deleting advertisements: {}", e.getMessage(), e);
        }
    }
    @Override
    public Advertisement getById(Long id) {
        try {
            logger.info("Fetching advertisement by ID: {}", id);
            Advertisement advertisement = advertisementRepository.getById(id);
            if (advertisement == null) {
                logger.warn("Advertisement with ID {} not found.", id);
            } else {
                logger.info("Advertisement with ID {} found.", id);
            }
            return advertisement;
        } catch (Exception e) {
            logger.error("An error occurred while fetching advertisement with ID {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    }
