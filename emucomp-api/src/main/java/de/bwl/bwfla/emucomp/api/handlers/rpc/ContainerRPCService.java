package de.bwl.bwfla.emucomp.api.handlers.rpc;

import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.api.ContainerComponent;
import de.bwl.bwfla.emucomp.components.AbstractEaasComponent;
import de.bwl.bwfla.emucomp.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.grpc.*;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;

import javax.inject.Inject;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@GrpcService
public class ContainerRPCService extends ContainerServiceGrpc.ContainerServiceImplBase {

    @Inject
    NodeManager nodeManager;

    @Override
    public void startContainer(EmptyRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            final ContainerComponent component = nodeManager.getComponentTransformed(ContainerComponent.class);
            component.start();
            responseObserver.onNext(EmptyResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stopContainer(EmptyRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            final ContainerComponent component = nodeManager.getComponentTransformed(ContainerComponent.class);
            component.stop();
            responseObserver.onNext(EmptyResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            throw new RuntimeException(e);
        }
    }
}
