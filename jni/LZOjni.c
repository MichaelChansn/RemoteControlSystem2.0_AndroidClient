/* org_minilzo_common_LZOjni.c -- JNI implementation of LZO library

   This file is part of the LZOjni library.

   Copyright (C) 2013 BOUVIER-VOLAILLE Julien
   All Rights Reserved.

   The LZOjni library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of
   the License, or (at your option) any later version.

   The LZOjni library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with the LZO library; see the file COPYING.
   If not, write to the Free Software Foundation, Inc.,
   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

   BOUVIER-VOLAILLE Julien
   <bouviervj@gmail.com>

 */

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>

 
#include <minilzo.h>
#include <lzoconf.h>
#include <lzodefs.h>
#include <LZOjni.h>
 
JNIEXPORT jint JNICALL Java_org_minilzo_common_LZOjni_LZOCompress
  (JNIEnv* iEnv, jobject iObj, jbyteArray iInputArray, jint iInputSize, jbyteArray iOuputArray, jintArray iOutputSize) {
  
  
   // Initialize the library
   if (lzo_init() != LZO_E_OK)
   {
     printf("Unable to init LZO library : C-code");
     return -1;
   }
  
   // Initialize both buffers
   lzo_bytep aInputBytes =  (lzo_bytep) (*iEnv)->GetByteArrayElements( iEnv, iInputArray, 0 );
   lzo_bytep aOutputBytes = (lzo_bytep) (*iEnv)->GetByteArrayElements( iEnv, iOuputArray, 0 );
   
   lzo_uintp aOutputSize = (lzo_uintp) (*iEnv)->GetIntArrayElements( iEnv, iOutputSize, 0 );
   
   // prepare working memory
   lzo_voidp wrkmem = (lzo_voidp) malloc(LZO1X_1_MEM_COMPRESS);
   
   // Prepare the output buffer
  
   int result =  lzo1x_1_compress( aInputBytes , iInputSize,
                       	     aOutputBytes, (lzo_uintp) aOutputSize,
                             wrkmem );
                             
   (*iEnv)->ReleaseIntArrayElements( iEnv, iOutputSize, (jint*) aOutputSize, 0); // Commit the size
                             
   (*iEnv)->ReleaseByteArrayElements( iEnv, iInputArray, aInputBytes, JNI_ABORT); // Abort commit for input
   (*iEnv)->ReleaseByteArrayElements( iEnv, iOuputArray,  aOutputBytes, 0); // Commit for output buffer
                             
   free(wrkmem); // release working mem
                             
   return result;
 
}


JNIEXPORT jint JNICALL Java_org_minilzo_common_LZOjni_LZODecompress
  (JNIEnv* iEnv, jobject iObj, jbyteArray iInputArray, jint iInputSize, jbyteArray iOuputArray, jintArray iOutputSize) {
 
 	 // Initialize the library
   if (lzo_init() != LZO_E_OK)
   {
     printf("Unable to init LZO library : C-code");
     return -1;
   }
    
  
   // Initialize both buffers
   lzo_bytep aInputBytes =  (lzo_bytep) (*iEnv)->GetByteArrayElements( iEnv, iInputArray, 0 );
   lzo_bytep aOutputBytes = (lzo_bytep) (*iEnv)->GetByteArrayElements( iEnv, iOuputArray, 0 );
   
   lzo_uintp aOutputSize = (lzo_uintp) (*iEnv)->GetIntArrayElements( iEnv, iOutputSize, 0 );
   
   // prepare working memory
   lzo_voidp wrkmem = (lzo_voidp) malloc(LZO1X_1_MEM_COMPRESS);
   
   // Prepare the output buffer
  
   int result =  lzo1x_decompress ( aInputBytes , iInputSize,
                       	     aOutputBytes, (lzo_uintp) aOutputSize,
                             wrkmem );
                             
   (*iEnv)->ReleaseIntArrayElements( iEnv, iOutputSize, (jint*) aOutputSize, 0); // Commit the size
                             
   (*iEnv)->ReleaseByteArrayElements( iEnv, iInputArray, aInputBytes, JNI_ABORT); // Abort commit for input
   (*iEnv)->ReleaseByteArrayElements( iEnv, iOuputArray,  aOutputBytes, 0); // Commit for output buffer
                             
   free(wrkmem); // release working mem
  
   return result;
  
}

JNIEXPORT jint JNICALL Java_org_minilzo_common_LZOjni_LZODecompress_1safe
  (JNIEnv* iEnv, jobject iObj, jbyteArray iInputArray, jint iInputSize, jbyteArray iOuputArray, jintArray iOutputSize){
  
   // Initialize the library
   if (lzo_init() != LZO_E_OK)
   {
     printf("Unable to init LZO library : C-code");
     return -1;
   }
    
  
   // Initialize both buffers
   lzo_bytep aInputBytes =  (lzo_bytep) (*iEnv)->GetByteArrayElements( iEnv, iInputArray, 0 );
   lzo_bytep aOutputBytes = (lzo_bytep) (*iEnv)->GetByteArrayElements( iEnv, iOuputArray, 0 );
   
   lzo_uintp aOutputSize = (lzo_uintp) (*iEnv)->GetIntArrayElements( iEnv, iOutputSize, 0 );
   
   // prepare working memory
   lzo_voidp wrkmem = (lzo_voidp) malloc(LZO1X_1_MEM_COMPRESS);
   
   // Prepare the output buffer
  
   int result =  lzo1x_decompress_safe ( aInputBytes , iInputSize,
                       	     aOutputBytes, (lzo_uintp) aOutputSize,
                             wrkmem );
                             
   (*iEnv)->ReleaseIntArrayElements( iEnv, iOutputSize, (jint*) aOutputSize, 0); // Commit the size
                             
   (*iEnv)->ReleaseByteArrayElements( iEnv, iInputArray, aInputBytes, JNI_ABORT); // Abort commit for input
   (*iEnv)->ReleaseByteArrayElements( iEnv, iOuputArray,  aOutputBytes, 0); // Commit for output buffer
                             
   free(wrkmem); // release working mem
  
   return result;
  
}  
