package de.bwl.bwfla.emucomp.api.handlers.rpc;

import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.components.api.ContainerComponent;
import de.bwl.bwfla.emucomp.api.security.SessionManagerResolver;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.grpc.ContainerServiceGrpc;
import de.bwl.bwfla.emucomp.grpc.EmptyRequest;
import de.bwl.bwfla.emucomp.grpc.EmptyResponse;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;

import javax.inject.Inject;

@GrpcService
public class ContainerRPCService extends ContainerServiceGrpc.ContainerServiceImplBase {

    @Inject
    NodeManager nodeManager;

    @Inject
    SessionManagerResolver sessionManagerResolver;

    @Override
    public void startContainer(EmptyRequest request, StreamObserver<EmptyResponse> responseObserver) {
        sessionManagerResolver.getSessionManager();
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
