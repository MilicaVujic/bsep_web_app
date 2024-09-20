package com.example.security.service;

import com.example.security.model.*;
import com.example.security.repository.AddressRepository;
import com.example.security.repository.AdvertisementRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

@Service
public class AdvertisementRequestService implements  IAdvertisementRequestService{

    @Autowired
    private AdvertisementRequestRepository advertisementRequestRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressRepository addressRepository;
    Logger logger= LoggerFactory.getLogger(AdvertisementRequestService.class);

    @Override
    public ArrayList<AdvertisementRequest> getAll() {
        ArrayList<AdvertisementRequest> advertisementRequests = new ArrayList<>();
        try {
            logger.info("getAll method in AdvertisementRequestService started.");
            advertisementRequests = (ArrayList<AdvertisementRequest>) advertisementRequestRepository.findAll();
            logger.info("getAll method in AdvertisementRequestService ended.");
        }
        catch (Exception e) {
            logger.error("An error occurred while fetching advertisement requests: {}", e.getMessage(), e);
        }
        return advertisementRequests;
    }

    @Override
    public AdvertisementRequest save(AdvertisementRequest request,Long id) {

        AdvertisementRequest retVal = null;
        try {
            logger.info("save method in AdvertisementRequestService started.");
            request.setRequestStatus(RequestStatus.NEW);
            Client c=this.clientService.getByUserId(id);
            request.setClient(c);
            retVal =  this.advertisementRequestRepository.save(request);
            logger.info("save method in AdvertisementRequestService started.");
        }
        catch (Exception e) {
            logger.error("An error occurred while saving the request: {}", e.getMessage(), e);
        }
        return retVal;


    }

    @Override
    public AdvertisementRequest getById(Long id){
        AdvertisementRequest retVal = null;
        try {
            logger.info("GetById method in AdvertisementRequestService started.");
            retVal =  this.advertisementRequestRepository.getById(id);
            if(retVal == null) {
                logger.warn("Advertisement request with ID {} not found.", id);
            }
            else {
                logger.info("Advertisement request with ID {} found.", id);

            }
            return retVal;
        }catch (Exception e) {
            logger.error("An error occurred while getting advertisement request with ID {}: {}", id, e.getMessage(), e);
            return null;
        }


    }


    public Long getClientId(Long id) {
        Long clientId = null;
        try {
            logger.info("getClientId method in AdvertisementRequestService started for id: {}", id);
            clientId = this.advertisementRequestRepository.getClientId(id);
            logger.info("getClientId method in AdvertisementRequestService ended for id: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while fetching client id for advertisement request id {}: {}", id, e.getMessage(), e);
        }
        return clientId;
    }

    public Long getClientIdByRequest(Long id) {
        Long clientId = null;
        try {
            logger.info("getClientId method in AdvertisementRequestService started for id: {}", id);
            clientId = this.advertisementRequestRepository.getClientIdByRequest(id);
            logger.info("getClientId method in AdvertisementRequestService ended for id: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while fetching client id for advertisement request id {}: {}", id, e.getMessage(), e);
        }
        return clientId;
    }



    @Override
    public AdvertisementRequest update(AdvertisementRequest request) {
        AdvertisementRequest updatedRequest = null;
        try {
            logger.info("update method in AdvertisementRequestService started for request id: {}", request.getId());
            request.setRequestStatus(RequestStatus.ACCEPTED);
            updatedRequest = this.advertisementRequestRepository.save(request);
            logger.info("update method in AdvertisementRequestService ended for request id: {}", request.getId());
        } catch (Exception e) {
            logger.error("An error occurred while updating advertisement request id {}: {}", request.getId(), e.getMessage(), e);
        }
        return updatedRequest;
    }

    public Long getClientIdByRequestId(Long id){
        return this.advertisementRequestRepository.getClientIdByRequestId(id);
    }

    /*@Override
    public AdvertisementRequest update(AdvertisementRequest request){
        request.setRequestStatus(RequestStatus.ACCEPTED);
        return this.advertisementRequestRepository.save(request);
    }*/



    @Override
    public AdvertisementRequest rejectRequest(AdvertisementRequest request) {
        AdvertisementRequest updatedRequest = null;
        try {
            logger.info("rejectRequest method in AdvertisementRequestService started for request id: {}", request.getId());
            Client client = clientService.getById(request.getClient().getId());
            if (client == null) {
                throw new EntityNotFoundException("Client not found");
            }
            request.setClient(client);
            request.setRequestStatus(RequestStatus.REJECTED);
            updatedRequest = advertisementRequestRepository.save(request);
            logger.info("rejectRequest method in AdvertisementRequestService ended for request id: {}", request.getId());
        } catch (EntityNotFoundException e) {
            logger.error("Client not found for advertisement request id {}: {}", request.getId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An error occurred while rejecting advertisement request id {}: {}", request.getId(), e.getMessage(), e);
        }
        return updatedRequest;
    }

    @Override
    public ArrayList<AdvertisementRequest> findAllByClientId(Long id) {
        ArrayList<AdvertisementRequest> advertisementRequests = new ArrayList<>();
        try {
            logger.info("findAllByClientId method in AdvertisementRequestService started for client id: {}", id);
            advertisementRequests = advertisementRequestRepository.findAllByClientId(id);
            logger.info("findAllByClientId method in AdvertisementRequestService ended for client id: {}", id);
        } catch (Exception e) {
            logger.error("An error occurred while fetching advertisement requests for client id {}: {}", id, e.getMessage(), e);
        }
        return advertisementRequests;
    }


    public void deleteAll(ArrayList<AdvertisementRequest> advertisementRequests) {
        try {
            logger.info("deleteAll method in AdvertisementRequestService started.");
            advertisementRequestRepository.deleteAll(advertisementRequests);
            logger.info("deleteAll method in AdvertisementRequestService ended.");
        } catch (Exception e) {
            logger.error("An error occurred while deleting advertisement requests: {}", e.getMessage(), e);
        }
    }


}
