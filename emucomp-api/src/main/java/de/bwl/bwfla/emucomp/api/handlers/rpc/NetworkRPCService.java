package de.bwl.bwfla.emucomp.api.handlers.rpc;

import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.components.api.NetworkSwitchComponent;
import de.bwl.bwfla.emucomp.api.security.SessionManagerResolver;
import de.bwl.bwfla.emucomp.common.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.grpc.GenericResponse;
import de.bwl.bwfla.emucomp.grpc.NetworkSwitchServiceGrpc;
import de.bwl.bwfla.emucomp.grpc.UrlRequest;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;

import javax.inject.Inject;

//Debatable implementation, better stay HTTP1?
@GrpcService
public class NetworkRPCService extends NetworkSwitchServiceGrpc.NetworkSwitchServiceImplBase {

    @Inject
    NodeManager nodeManager;

    @Inject
    SessionManagerResolver sessionManagerResolver;

    @Override
    public void connect(UrlRequest request, StreamObserver<GenericResponse> responseObserver) {
        sessionManagerResolver.getSessionManager();
        final NetworkSwitchComponent comp;
        try {
            comp = nodeManager.getComponentTransformed(NetworkSwitchComponent.class);
            comp.connect(request.getUrl());

            responseObserver.onNext(GenericResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnect(UrlRequest request, StreamObserver<GenericResponse> responseObserver) {
        final NetworkSwitchComponent comp;
        try {
            comp = nodeManager.getComponentTransformed(NetworkSwitchComponent.class);
            comp.disconnect(request.getUrl());

            responseObserver.onNext(GenericResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            throw new RuntimeException(e);
        }
    }
}
