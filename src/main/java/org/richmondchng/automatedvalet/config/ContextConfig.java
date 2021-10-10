package org.richmondchng.automatedvalet.config;

import org.richmondchng.automatedvalet.controller.ParkingValetController;
import org.richmondchng.automatedvalet.data.repository.ParkedVehicleRepository;
import org.richmondchng.automatedvalet.data.repository.ParkedVehicleRepositoryImpl;
import org.richmondchng.automatedvalet.data.repository.ParkingFeeRepository;
import org.richmondchng.automatedvalet.data.repository.ParkingFeeRepositoryImpl;
import org.richmondchng.automatedvalet.data.repository.ParkingLotRepository;
import org.richmondchng.automatedvalet.data.repository.ParkingLotRepositoryImpl;
import org.richmondchng.automatedvalet.data.storage.ParkedVehicleDataStorage;
import org.richmondchng.automatedvalet.data.storage.ParkingFeeDataStorage;
import org.richmondchng.automatedvalet.data.storage.ParkingLotDataStorage;
import org.richmondchng.automatedvalet.model.vehicle.VehicleType;
import org.richmondchng.automatedvalet.service.ParkingFeeService;
import org.richmondchng.automatedvalet.service.ParkingValetService;

import java.util.HashMap;
import java.util.Map;

/**
 * For setting up application context.
 *
 * Note: This will be a similar structure if we are using "Spring". Can be replaced (in future) with Spring Framework.
 *
 * @author richmondchng
 */
public class ContextConfig {

    private final ParkingLotConfiguration[] configurations;

    /**
     * Constructor.
     * @param parkingLotConfigurations array of parking lot configurations
     */
    public ContextConfig(final ParkingLotConfiguration[] parkingLotConfigurations) {
        this.configurations = parkingLotConfigurations;
    }

    /**
     * Create controller.
     * @return ParkingValetController
     */
    public ParkingValetController parkingValetController() {
        // data storage
        final ParkingLotDataStorage parkingLotDataStorage = parkingLotDataStorage(configurations);
        final ParkedVehicleDataStorage parkedVehicleDataStorage = parkedVehicleDataStorage();
        final ParkingFeeDataStorage parkingFeeDataStorage = parkingFeeDataStorage(configurations);
        // repository
        final ParkingLotRepository parkingLotRepository = parkingLotRepository(parkingLotDataStorage);
        final ParkedVehicleRepository parkedVehicleRepository = parkedVehicleRepository(parkedVehicleDataStorage);
        final ParkingFeeRepository parkingFeeRepository = parkingFeeRepository(parkingFeeDataStorage);
        // service
        final ParkingValetService parkingValetService = parkingValetService(parkingLotRepository, parkedVehicleRepository);
        final ParkingFeeService parkingFeeService = parkingFeeService(parkingFeeRepository, parkedVehicleRepository);
        // controller
        return new ParkingValetController(parkingValetService, parkingFeeService);
    }

    /**
     * Create ParkingValetService.
     * @param parkingLotRepository ParkingLotRepository
     * @param parkedVehicleRepository ParkedVehicleRepository
     * @return ParkingValetService
     */
    private ParkingValetService parkingValetService(final ParkingLotRepository parkingLotRepository,
                                                    final ParkedVehicleRepository parkedVehicleRepository) {
        return new ParkingValetService(parkingLotRepository, parkedVehicleRepository);
    }

    /**
     * Create ParkingFeeService.
     * @param parkingFeeRepository ParkingFeeRepository
     * @param parkedVehicleRepository ParkedVehicleRepository
     * @return ParkingFeeService
     */
    private ParkingFeeService parkingFeeService(final ParkingFeeRepository parkingFeeRepository,
                                                final ParkedVehicleRepository parkedVehicleRepository) {
        return new ParkingFeeService(parkingFeeRepository, parkedVehicleRepository);
    }

    /**
     * Create ParkingLotRepository.
     * @param parkingLotDataStorage ParkingLotDataStorage
     * @return ParkingLotRepository
     */
    private ParkingLotRepository parkingLotRepository(final ParkingLotDataStorage parkingLotDataStorage) {
        return new ParkingLotRepositoryImpl(parkingLotDataStorage);
    }

    /**
     * Create ParkedVehicleRepository.
     * @param parkedVehicleDataStorage ParkedVehicleDataStorage
     * @return ParkedVehicleRepository
     */
    private ParkedVehicleRepository parkedVehicleRepository(final ParkedVehicleDataStorage parkedVehicleDataStorage) {
        return new ParkedVehicleRepositoryImpl(parkedVehicleDataStorage);
    }

    /**
     * Create ParkingFeeRepository.
     * @param parkingFeeDataStorage ParkingFeeDataStorage
     * @return ParkingFeeRepository
     */
    private ParkingFeeRepository parkingFeeRepository(final ParkingFeeDataStorage parkingFeeDataStorage) {
        return new ParkingFeeRepositoryImpl(parkingFeeDataStorage);
    }

    /**
     * Create ParkingLotDataStorage.
     * @param details configurations of parking lots
     * @return ParkingLotDataStorage
     */
    private ParkingLotDataStorage parkingLotDataStorage(final ParkingLotConfiguration[] details) {
        final Map<VehicleType, Integer> map = new HashMap<>();
        for(ParkingLotConfiguration detail : details) {
            map.put(detail.getVehicleType(), detail.getNumberOfLots());
        }
        return new ParkingLotDataStorage(map);
    }

    /**
     * Create ParkedVehicleDataStorage.
     * @return ParkedVehicleDataStorage
     */
    private ParkedVehicleDataStorage parkedVehicleDataStorage() {
        return new ParkedVehicleDataStorage();
    }

    /**
     * Create ParkingFeeDataStorage.
     * @param details configurations of parking lots
     * @return ParkingFeeDataStorage
     */
    private ParkingFeeDataStorage parkingFeeDataStorage(final ParkingLotConfiguration[] details) {
        final Map<VehicleType, Integer> map = new HashMap<>();
        for(ParkingLotConfiguration detail : details) {
            map.put(detail.getVehicleType(), detail.getFeePerHours());
        }
        return new ParkingFeeDataStorage(map);
    }
}
