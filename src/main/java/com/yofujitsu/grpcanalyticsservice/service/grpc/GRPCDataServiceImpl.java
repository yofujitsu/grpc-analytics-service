package com.yofujitsu.grpcanalyticsservice.service.grpc;

import com.yofujitsu.grpcanalyticsservice.entity.data.Data;
import com.yofujitsu.grpcanalyticsservice.service.summary.SummaryService;
import com.yofujitsu.grpccommon.AnalyticsServerGrpc;
import com.yofujitsu.grpccommon.GRPCAnalyticsRequest;
import com.yofujitsu.grpccommon.GRPCData;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class GRPCDataServiceImpl implements GRPCDataService {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final SummaryService summaryService;

    @GrpcClient(value = "data-store-async")
    private AnalyticsServerGrpc.AnalyticsServerStub asyncStub;

    @Value("${fetch.batch-size}")
    private int batchSize;

    /**
     * This method is annotated with @PostConstruct, which means it will be automatically invoked after the dependency injection
     * is done to perform any initialization.
     *
     * In this case, it is used to invoke the fetchMessages() method to initiate the GRPC stream of data fetching.
     */
    @PostConstruct
    public void init() {
        fetchMessages();
    }

    /**
     * This method fetches messages from the gRPC server and handles them
     * in a scheduled manner.
     */
    @Override
    public void fetchMessages() {
        // Schedule the execution of the fetchMessages gRPC requests
        executorService.scheduleAtFixedRate(() -> asyncStub.askForData(
                GRPCAnalyticsRequest.newBuilder()
                        .setBatchSize(batchSize)
                        .build(),
                new StreamObserver<GRPCData>() {
                    /**
                     * This method is called when a new message is received from the gRPC server.
                     * It transforms the GRPCData message into a Data object and passes it to the summaryService
                     * to be handled.
                     * @param grpcData The GRPCData message received from the gRPC server.
                     */
                    @Override
                    public void onNext(GRPCData grpcData) {
                        summaryService.handle(new Data(grpcData));
                    }

                    /**
                     * This method is called when an error occurs during the processing of the gRPC stream.
                     * It logs the error and does not take any further action.
                     * @param throwable The exception that occurred.
                     */
                    @Override
                    public void onError(Throwable throwable) {
                        log.error("Batch was not handled", throwable);
                    }

                    /**
                     * This method is called when the gRPC stream has been successfully processed.
                     * It logs a message indicating that the batch was handled.
                     */
                    @Override
                    public void onCompleted() {
                        log.info("Batch was handled");
                    }
                }
        ), 0, 10, java.util.concurrent.TimeUnit.SECONDS);
    }
}
