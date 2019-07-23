package com.example.sven.zip.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class ZipHelper {


    /**
     *  7zr a  [输出文件] [待压缩文件/目录] -mx=9
     * @param context
     * @param srcPath
     * @param outPath
     * @param listener
     */
    public void pack(Context context,String srcPath,String outPath,OnResultListener listener){
        File src = new File(srcPath);
        File out = new File(outPath);
        execute(context,"7zr a "+out.getAbsolutePath()+" "+src.getAbsolutePath()+" -mx=9",listener);
    }


    /**
     * 7zr x [压缩文件]  -o[输出目录]
     * @param srcPath 压缩文件
     * @param outPath 输出目录
     */
    //解压
    public void unpack(Context context,String srcPath,String outPath,OnResultListener listener){
        File src = new File(srcPath);
        File out = new File(outPath);
        execute(context,"7zr x "+src.getAbsolutePath()+" -o"+out.getAbsolutePath()
                    ,listener);
    }


    /**
     * 执行结果回调
     */
    public interface OnResultListener {
        void onSuccess(String msg);

        void onFailure(int errorno, String msg);

        void onProgress(String msg);
    }


    public  void execute(Context context, String cmd, OnResultListener listener) {
        File filesDir = context.getFilesDir();
        // /data/data/包名/7zr
        new ExecuteAysnTask(filesDir.getAbsolutePath() + "/" + cmd, listener).execute();
    }

    private class ExecuteAysnTask extends AsyncTask<Void,String,Result>{

        private OnResultListener listener;
        private String cmd;


        public ExecuteAysnTask(String cmd, OnResultListener listener) {
            this.cmd = cmd;
            this.listener = listener;
        }

        @Override
        protected Result doInBackground(Void... voids) {

            //执行结果输出
            String out;
            Process process = null;
            try{
                //执行任务
                 process = Runtime.getRuntime().exec(cmd);
                 while(!isComplete(process)){
                     //读取运行过程中的输出信息
                     BufferedReader reader = new BufferedReader(new InputStreamReader
                             (process.getInputStream()));
                     String line;
                     while ((line = reader.readLine()) != null) {
                         //报告执行过程
                         publishProgress(line);
                     }
                 }
                int exitValue = process.exitValue();
                 //成功
                if(exitValue == 0){
                    out = CommandUtils.inputStream2String(process.getInputStream());
                }else{
                    out = CommandUtils.inputStream2String(process.getErrorStream());
                }
                return new Result(exitValue == 0,out,exitValue);
            }catch (Exception e){
                e.printStackTrace();
                out = e.getMessage();
            }finally {
                if (null != process) {
                    process.destroy();
                }
            }
            return new Result(false,out,-1);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            listener.onProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Result result) {
            if(result.success){
                listener.onSuccess(result.output);
            }else{
                listener.onFailure(result.errorno, result.output);
            }
        }


        /**
         * 查看程序是否结束
         *
         * @param process
         * @return
         */
        private boolean isComplete(Process process) {
            try {
                //如果已经结束则返回结果 否则会出现IllegalThreadStateException异常
                process.exitValue();
                return true;
            } catch (IllegalThreadStateException e) {
            }
            return false;
        }

    }

    /**
     * 结果记录
     */
    private class Result {
        boolean success;
        String output;
        int errorno;

        public Result(boolean success, String output, int errorno) {
            this.success = success;
            this.output = output;
            this.errorno = errorno;
        }
    }


}

