package de.bwl.bwfla.emucomp.api.handlers.rpc;

import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.api.security.SessionManagerResolver;
import de.bwl.bwfla.emucomp.components.AbstractEaasComponent;
import de.bwl.bwfla.emucomp.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.grpc.*;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;

import javax.inject.Inject;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@GrpcService
public class ComponentRPCService extends ComponentServiceGrpc.ComponentServiceImplBase {

    @Inject
    NodeManager nodeManager;

    @Inject
    SessionManagerResolver sessionManagerResolver;

    @Override
    public void initializeComponent(ComponentRequest request, StreamObserver<GenericResponse> responseObserver) {
        sessionManagerResolver.getSessionManager();
        try {
            String s = nodeManager.allocateComponent(request.getComponentId(), request.getComponentConfiguration());
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(s)
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void destroyComponent(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        nodeManager.releaseComponent();
        responseObserver.onNext(GenericResponse.newBuilder().setMessage("Component destroyed").build());
        responseObserver.onCompleted();
    }

    @Override
    public void keepAliveComponent(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        try {
            nodeManager.keepalive();
            responseObserver.onNext(GenericResponse.newBuilder().setMessage("Component keep alive").build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void state(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final AbstractEaasComponent component;
        try {
            component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(component.getState().toString())
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void type(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final AbstractEaasComponent component;
        try {
            component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(component.getComponentType())
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void environmentId(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final AbstractEaasComponent component;
        try {
            component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(component.getEnvironmentId())
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void controlUrls(EmptyRequest request, StreamObserver<MapResponse> responseObserver) {

        final AbstractEaasComponent component;
        try {
            component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);
            Map<String, URI> controlUrls = component.getControlUrls();

            Map<String, String> transformedMap = controlUrls.entrySet()
                    .stream()
                    .collect(
                            Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> e.getValue().getRawPath()
                            )
                    );
            responseObserver.onNext(MapResponse.newBuilder()
                    .putAllResponseMap(transformedMap)
                    .build()
            );
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void eventSourceUrls(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final AbstractEaasComponent component;
        try {
            component = nodeManager.getComponentTransformed(AbstractEaasComponent.class);

            responseObserver.onNext(
                    GenericResponse.newBuilder()
                            .setMessage(component.getEventSourceUrl().getRawPath())
                            .build()
            );
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }
}
