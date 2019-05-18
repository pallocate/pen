package pen.eco.common

import com.sun.jna.Library
import com.sun.jna.NativeLong

/** An limited interface to the Sodium library. */
interface Sodium : Library
{
   fun sodium_init () : Int
   fun randombytes_buf (buf : ByteArray, size : Int )

   /* HMAC */
   fun crypto_generichash (output : ByteArray, outlen : Int, input : ByteArray, inlen : Int, key : ByteArray?, keylen : Int) : Int
//   fun crypto_generichash_keybytes () : NativeLong

   /* Keying */
   fun crypto_sign_seed_keypair (pk : ByteArray, sk : ByteArray, seed : ByteArray) : Int
   fun crypto_sign_ed25519_sk_to_curve25519 (curve25519_sk : ByteArray, ed25519_sk : ByteArray) : Int
   fun crypto_sign_ed25519_pk_to_curve25519 (curve25519_pk : ByteArray, ed25519_pk : ByteArray) : Int
   fun crypto_pwhash (key : ByteArray, keylen : Long, passwd : ByteArray, passwdlen: Long, salt : ByteArray, opslimit : Long, memlimit : NativeLong, alg : Int) : Int
   fun crypto_pwhash_alg_argon2id13 () : Int
   fun crypto_pwhash_saltbytes () : Int
   fun crypto_pwhash_opslimit_moderate () : Long
   fun crypto_pwhash_memlimit_moderate () : NativeLong

   /* Signing */
   fun crypto_sign_ed25519 (sm : ByteArray, smlen_p: Long, m : ByteArray, mlen : Long, sk : ByteArray) : Int
   fun crypto_sign_ed25519_open (m : ByteArray, mlen_p : Long, sm : ByteArray, smlen : Long, pk : ByteArray) : Int
   fun crypto_sign_ed25519_detached (sig : ByteArray, siglen : Long, m : ByteArray, mlen : Long, sk : ByteArray) : Int
   fun crypto_sign_ed25519_verify_detached (sig : ByteArray, m : ByteArray, mlen : Long, pk : ByteArray) : Int
   fun crypto_sign_seedbytes () : NativeLong
   fun crypto_sign_ed25519_secretkeybytes () : NativeLong
   fun crypto_sign_ed25519_publickeybytes () : NativeLong
   fun crypto_sign_ed25519_bytes() : Int

   /* Public key encryption */
   fun crypto_box_easy (c : ByteArray, m : ByteArray, mlen : Long, n : ByteArray, pk : ByteArray, sk : ByteArray) : Int
   fun crypto_box_open_easy (m : ByteArray, c : ByteArray, clen : Long, n : ByteArray, pk : ByteArray, sk : ByteArray) : Int
   fun crypto_box_publickeybytes () : NativeLong
   fun crypto_box_secretkeybytes () : NativeLong
   fun crypto_box_noncebytes () : NativeLong
   fun crypto_box_macbytes () : NativeLong

   /* Secret key encryption */
   fun crypto_secretbox_easy (c : ByteArray, m : ByteArray, mlen : Long, n : ByteArray, k : ByteArray) : Int
   fun crypto_secretbox_open_easy (m : ByteArray, c : ByteArray, clen : Long, n : ByteArray, k : ByteArray) : Int
   fun crypto_secretbox_keybytes () : NativeLong
   fun crypto_secretbox_noncebytes () : NativeLong
   fun crypto_secretbox_macbytes () : NativeLong
}
