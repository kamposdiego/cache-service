package bhsg.com.cache.service;

import bhsg.com.cache.*;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@RequiredArgsConstructor
@GrpcService
public class IdempotentService extends IdempotentServiceGrpc.IdempotentServiceImplBase {

    private final RedisService redisService;

    @Override
    public void getByXIdempotencyKey(IdempotentByXIdempotencyRequest request, StreamObserver<IdempotentReply> responseObserver) {
        responseObserver.onNext(redisService.getByXIdempotencyId(request.getXIdempotencyKey()));
        responseObserver.onCompleted();
    }

    @Override
    public void save(IdempotentRequest request, StreamObserver<IdempotentReply> responseObserver) {
        responseObserver.onNext(redisService.createPostRequest(request.getId()));
        responseObserver.onCompleted();
    }

}
