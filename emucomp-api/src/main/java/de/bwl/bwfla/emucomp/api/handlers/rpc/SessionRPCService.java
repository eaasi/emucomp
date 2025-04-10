package de.bwl.bwfla.emucomp.api.handlers.rpc;

import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.api.security.SessionManager;
import de.bwl.bwfla.emucomp.api.security.SessionManagerResolver;
import de.bwl.bwfla.emucomp.grpc.SessionRequest;
import de.bwl.bwfla.emucomp.grpc.SessionResponse;
import de.bwl.bwfla.emucomp.grpc.SessionServiceGrpc;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;

import javax.inject.Inject;
import java.util.UUID;

@GrpcService
public class SessionRPCService extends SessionServiceGrpc.SessionServiceImplBase {

    @Inject
    NodeManager nodeManager;

    @Inject
    SessionManagerResolver sessionManagerResolver;

    @Override
    public void createSession(SessionRequest request, StreamObserver<SessionResponse> responseObserver) {
        SessionManager sessionManager = sessionManagerResolver.getSessionManager();
        String sessionId = UUID.randomUUID().toString();

        sessionManager.registerSession(sessionId);
        responseObserver.onNext(SessionResponse.newBuilder()
                .setSessionId(sessionId)
                .build());
        responseObserver.onCompleted();
    }
}
