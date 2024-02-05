package com.hyun.jobty.global.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    // 스레드 풀내에서 작업 처리 불가 시
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 해당 요청 무시
        // AbortPolicy: 예외 발생 후 종료
        // CallerRunsPolicy: 스레드풀을 호출한 스레드에서 처리
        // DiscardPolicy: 해당 요청 무시
        // DiscardOldestPolicy: 큐에 있는 가장 오래된 요청 삭제 및 새로운 요청 큐에 추가
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
