package com.noname.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by noname on 2019/8/12.
 * <p>
 * Master对象是用于管理所有的Worker的处理和汇总
 */
public class Demo18_Master {

    //该队列用于存放所有的任务
    private ConcurrentLinkedQueue<Demo18_Task> workQueue = new ConcurrentLinkedQueue<>();
    //用于存放所有的Worker
    private Map<String, Thread> workers = new HashMap<String, Thread>();
    //用于存放所有的Worker处理完后的返回结果
    private ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();

    //构造方法，将所有的Worker都放入workers对象中
    public Demo18_Master(Demo18_Worker worker, int workerCount) {
        worker.setWorkQueue(workQueue);
        worker.setResultMap(resultMap);
        for (int x = 0; x < workerCount; x++) {
            workers.put("子节点:" + x, new Thread(worker));
        }
    }


    /**
     * 将任务添加到任务队列
     *
     * @param task
     */
    public void submit(Demo18_Task task) {
        workQueue.add(task);
    }

    /**
     * 启动所有Worker
     */
    public void execute() {
        for (Map.Entry<String, Thread> me : workers.entrySet()) {
            me.getValue().start();
        }
    }

    /**
     * 判断是否所有线程都已经完成了任务
     *
     * @return
     */
    public boolean isCompleted() {
        for (Map.Entry<String, Thread> me : workers.entrySet()) {
            if (me.getValue().getState() != Thread.State.TERMINATED) {      //若所有的线程并非暂停状态，则代表还有未执行完成的任务
                return false;
            }
        }
        return true;
    }

    /**
     * 数据总结，这边假设业务为结果求和
     *
     * @return
     */
    public int getResult() {
        int ret = 0;
        for (Map.Entry<String, Object> me : resultMap.entrySet()) {
            ret += (int) me.getValue();
        }
        return ret;
    }
}
