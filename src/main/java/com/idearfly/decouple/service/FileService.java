package com.idearfly.decouple.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.idearfly.decouple.DecoupleConfiguration;
import com.idearfly.decouple.vo.FileObject;
import com.idearfly.decouple.vo.FileSupport;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileService {
    private static final String RootDirectory = System.getProperty("user.dir");
    private static final String ApiDirectory;
    static {
        ApiDirectory = RootDirectory + DecoupleConfiguration.httpApi;

        System.out.println("ApiDirectory: " + ApiDirectory);

        File file = new File(ApiDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public String filePath(HttpServletRequest request) {
        return filePath(request.getServletPath());
    }

    public String filePath(String servletPath) {
        for (Map.Entry<String, FileSupport> entry: DecoupleConfiguration.FileSupport.entrySet()) {
            FileSupport fileSupport = entry.getValue();
            Pattern pattern = Pattern.compile(fileSupport.getManager());
            Matcher matcher = pattern.matcher(servletPath);
            if (matcher.lookingAt()) {
                return RootDirectory + matcher.replaceFirst(DecoupleConfiguration.httpApi) + "." +entry.getKey();
            }
        }
        String path = servletPath.replaceAll("^"+ DecoupleConfiguration.httpManager, DecoupleConfiguration.httpApi);
        return RootDirectory + path;
    }

    public String readContent(HttpServletRequest request) {
        String path = filePath(request);
        FileInputStream fileInputStream = null;
        BufferedReader reader = null;
        StringBuilder laststr = new StringBuilder();
        try{
            fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while((tempString = reader.readLine()) != null){
                laststr.append(tempString);
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(fileInputStream != null) {
                try {
                  fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr.toString();
    }

    public String writeContent(HttpServletRequest request, String text) {
        String path = filePath(request);
        File file = new File(path);
        mkdirs(file);

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public JSONObject readJSONObject(HttpServletRequest request) {
        String path = filePath(request);
        return readJSONObject(path);
    }

    public JSONObject readJSONObject(String path) {
        return readJSONObject(new File(path));
    }

    public JSONObject readJSONObject(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            JSONObject jsonObject = JSONObject.parseObject(
                    fileInputStream,
                    IOUtils.UTF8,
                    JSONObject.class);
            return jsonObject;
        } catch (IOException e) {
            //e.printStackTrace();
        } finally{
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    public JSONObject writeJSONObjectProperty(
            HttpServletRequest request,
            String oldKey,
            String newKey,
            JSONObject jsonObject) {
        String path = filePath(request);
        JSONObject oldJSONObject = readJSONObject(path);
        if (oldJSONObject == null) {
            oldJSONObject = new JSONObject();
        } else {
            if (oldKey == null || oldKey.length() > 0) {
                oldJSONObject.remove(oldKey);
            }
        }
        oldJSONObject.put(newKey, jsonObject);
        writeJSONObject(path, oldJSONObject);
        return oldJSONObject;
    }

    public JSONObject deleteProperty(
            HttpServletRequest request,
            String key) {
        String path = filePath(request);
        JSONObject oldJSONObject = readJSONObject(path);
        if (oldJSONObject == null) {
            return new JSONObject();
        } else {
            if (key == null || key.length() > 0) {
                oldJSONObject.remove(key);
            }
        }
        writeJSONObject(path, oldJSONObject);
        return oldJSONObject;
    }

    public void writeJSONObject(HttpServletRequest request, JSONObject jsonObject) {
        String path = filePath(request);
        writeJSONObject(path, jsonObject);
    }

    public void writeJSONObject(String path, JSONObject jsonObject) {
        File file = new File(path);
        mkdirs(file);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            JSONObject.writeJSONString(
                    fileOutputStream,
                    jsonObject);
        } catch (IOException e) {
            //e.printStackTrace();
        } finally{
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<FileObject> listFiles(HttpServletRequest request) {
        String path = filePath(request);
        return listFiles(path);
    }

    public List<FileObject> listFiles(String path) {
        return listFiles(new File(path));
    }

    public List<FileObject> listFiles(File file) {
        if (!file.isDirectory()) {
            return null;
        }
        File[] files = file.listFiles();
        List<FileObject> list = new ArrayList<>(files.length);
        for (File f: files) {
            FileObject fileObject = new FileObject();
            fileObject.setFilename(f.getName());
            fileObject.setPath(f.getPath());
            fileObject.setType(f.isFile());
            extraFileObject(fileObject);
            list.add(fileObject);
        }
        return list;
    }

    public Boolean delete(HttpServletRequest request) {
        String path = filePath(request);
        File file = new File(path);
        return deleteFile(file);
    }

    public Boolean rename(HttpServletRequest request, String from, String to) {
        String fromPath = filePath(from);
        String toPath = filePath(to);
        File fromFile = new File(fromPath);
        File toFile = new File(toPath);
        mkdirs(toFile);
        return fromFile.renameTo(toFile);
    }

    private Boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
            return file.delete();
        }
    }

    private void mkdirs(File file) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
    }

    private void extraFileObject(FileObject fileObject) {
        String filename = fileObject.getFilename();
        int dot = filename.lastIndexOf('.');
        if (dot < 0) {
            fileObject.setName(filename);
            fileObject.setExt("");
            return;
        }
        fileObject.setName(filename.substring(0, dot));
        fileObject.setExt(filename.substring(dot + 1));
    }
}
