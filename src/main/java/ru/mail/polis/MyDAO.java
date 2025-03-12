// package ru.mail.polis;

// import java.io.File;
// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.OutputStream;
// import java.util.NoSuchElementException;

// import org.jetbrains.annotations.NotNull;

// public class MyDAO implements KVDao {

//     @NotNull
//     private final File dir;

//     public MyDAO(@NotNull final File dir){
//         this.dir = dir; 
//     }

//     public void close() throws IOException {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'close'");
//     }

//     @Override
//     public byte[] get(@NotNull String key) throws NoSuchElementException, IOException {
//         final File file = new File(dir, key);
//         if(!file.exists()){
//             throw new NoSuchElementException();
//         }
//         final byte[] value = new byte[(int) file.length()];
//         try(InputStream is = new FileInputStream(file)){
//             if (is.read(value) != value.length){
//                 throw new IOException("Can't read file");
//             } 
//             return value;
//         }
//     }

//     @Override
//     public void upsert(@NotNull final String key, @NotNull final byte[] value) throws IOException {
//         try(OutputStream os = new FileOutputStream(new File(dir, key))) {
//             os.write(value);
//         }
//     }

//     @Override
//     public void remove(@NotNull final String key) throws IOException {
//         final File file = new File(dir, key);
//         file.delete();
//     }
    
// }
