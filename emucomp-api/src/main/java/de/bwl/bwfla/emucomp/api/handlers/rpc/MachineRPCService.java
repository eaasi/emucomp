package de.bwl.bwfla.emucomp.api.handlers.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bwl.bwfla.emucomp.NodeManager;
import de.bwl.bwfla.emucomp.ProcessMonitorVID;
import de.bwl.bwfla.emucomp.api.EmulatorComponent;
import de.bwl.bwfla.emucomp.api.security.SessionManagerResolver;
import de.bwl.bwfla.emucomp.exceptions.BWFLAException;
import de.bwl.bwfla.emucomp.grpc.*;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;

import javax.activation.DataHandler;
import javax.inject.Inject;
import java.util.stream.Collectors;

@GrpcService
public class MachineRPCService extends MachineServiceGrpc.MachineServiceImplBase {

    @Inject
    NodeManager nodeManager;

    @Inject
    SessionManagerResolver sessionManagerResolver;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void startMachine(EmptyRequest request, StreamObserver<EmptyResponse> responseObserver) {
        sessionManagerResolver.getSessionManager();
        try {
            final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            emul.start();
            responseObserver.onNext(EmptyResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void stopMachine(EmptyRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            emul.stop();
            responseObserver.onNext(EmptyResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void changeMedium(MediumRequest request, StreamObserver<GenericResponse> responseObserver) {
        try {
            final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(String.valueOf(emul.changeMedium(request.getHandle(), request.getObjReference())))
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void attachMedium(MediumRequest request, StreamObserver<GenericResponse> responseObserver) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            DataHandler dataHandler = objectMapper.readValue(request.getDataHandler(), DataHandler.class);

            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(String.valueOf(emul.attachMedium(dataHandler, request.getMediaType())))
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException | JsonProcessingException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void detachMedium(MediumRequest request, StreamObserver<GenericResponse> responseObserver) {

        try {
            final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);

            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(String.valueOf(emul.detachMedium(request.getHandle())))
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void runtimeConfiguration(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(emul.getRuntimeConfiguration())
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void coldDrives(EmptyRequest request, StreamObserver<ListResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(ListResponse.newBuilder()
                    .addAllResponseList(emul.getColdplugableDrives())
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void hotDrives(EmptyRequest request, StreamObserver<ListResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(ListResponse.newBuilder()
                    .addAllResponseList(emul.getHotplugableDrives())
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void snapshot(EmptyRequest request, StreamObserver<ListResponse> responseObserver) {
        final EmulatorComponent emul;

        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(
                    ListResponse.newBuilder()
                            .addAllResponseList(
                                    emul.snapshot().stream()
                                            .map(x -> {
                                                try {
                                                    return mapper.writeValueAsString(x);
                                                } catch (JsonProcessingException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            })
                                            .collect(Collectors.toList())
                            )
                            .build()
            );
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void emulatorState(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(emul.getEmulatorState())
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void checkpoint(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(mapper.writeValueAsString(emul.checkpoint()))
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException | JsonProcessingException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void prepareSessionRecorder(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(String.valueOf(emul.prepareSessionRecorder()))
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void startSessionRecording(EmptyRequest request, StreamObserver<EmptyResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            emul.startSessionRecording();
            responseObserver.onNext(EmptyResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void stopSessionRecording(EmptyRequest request, StreamObserver<EmptyResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            emul.stopSessionRecording();
            responseObserver.onNext(EmptyResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void isRecordModeEnabled(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(String.valueOf(emul.isRecordModeEnabled()))
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void addActionFinishedMark(EmptyRequest request, StreamObserver<EmptyResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            emul.addActionFinishedMark();

            responseObserver.onNext(EmptyResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void defineTraceMetadataChunk(MetadataChunkRequest request, StreamObserver<EmptyResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            emul.defineTraceMetadataChunk(request.getTag(), request.getComment());

            responseObserver.onNext(EmptyResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void addTraceMetadataEntry(MetadataEntryRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            final EmulatorComponent emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            emul.addTraceMetadataEntry(request.getCtag(), request.getKey(), request.getValue());

            responseObserver.onNext(EmptyResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void sessionTrace(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(emul.getSessionTrace())
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void prepareSessionPlayer(SessionPlayerRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);

            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(String.valueOf(emul.prepareSessionPlayer(request.getTrace(), request.getHeadless())))
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void sessionPlayerProgress(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(String.valueOf(emul.getSessionPlayerProgress()))
                    .build());
            responseObserver.onCompleted();

        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void isReplayModeEnabled(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(String.valueOf(emul.isReplayModeEnabled()))
                    .build());
            responseObserver.onCompleted();

        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateMonitorValues(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);

            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(String.valueOf(emul.updateMonitorValues()))
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void monitorValue(ProcessMonitorVid request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);

            responseObserver.onNext(GenericResponse.newBuilder()
                    .setMessage(emul.getMonitorValue(ProcessMonitorVID.valueOf(request.getEnumVal())))
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void monitorValues(MonitorValuesRequest request, StreamObserver<ListResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);

            responseObserver.onNext(ListResponse.newBuilder()
                    .addAllResponseList(emul.getMonitorValues(request.getReqsList()
                            .stream().map(e -> ProcessMonitorVID.valueOf(e.getEnumVal()))
                            .collect(Collectors.toList())))
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void allMonitorValues(EmptyRequest request, StreamObserver<ListResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);

            responseObserver.onNext(ListResponse.newBuilder()
                    .addAllResponseList(emul.getAllMonitorValues())
                    .build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void printJobs(EmptyRequest request, StreamObserver<PrintJobResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);

            responseObserver.onNext(PrintJobResponse.newBuilder()
                    .addAllJobs(
                            emul.getPrintJobs()
                                    .stream().map(e -> {
                                        try {
                                            return PrintJob.newBuilder()
                                                    .setLabel(e.getLabel())
                                                    .setDataHandler(mapper.writeValueAsString(e.getDataHandler()))
                                                    .build();
                                        } catch (JsonProcessingException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }).collect(Collectors.toList())).build());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void takeScreenshot(EmptyRequest request, StreamObserver<EmptyResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            emul.takeScreenshot();

            responseObserver.onNext(EmptyResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (BWFLAException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void nextScreenshot(EmptyRequest request, StreamObserver<GenericResponse> responseObserver) {
        final EmulatorComponent emul;
        try {
            emul = nodeManager.getComponentTransformed(EmulatorComponent.class);
            DataHandler nextScreenshot = emul.getNextScreenshot();

            responseObserver.onNext(
                    GenericResponse.newBuilder()
                            .setMessage(mapper.writeValueAsString(nextScreenshot))
                            .build()
            );
            responseObserver.onCompleted();
        } catch (BWFLAException | JsonProcessingException e) {
            responseObserver.onError(e);
        }
    }
}
