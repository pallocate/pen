package pen.eco

import kotlinx.serialization.json.Json

fun deserializePQ (jsonString : String) = Json.decodeFromString<KProductQuantities>( KProductQuantities.serializer(), jsonString )
fun serializePQ (pq : KProductQuantities) = Json.encodeToString( KProductQuantities.serializer(), pq )
