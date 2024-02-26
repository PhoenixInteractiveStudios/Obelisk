/**
 * To simplify both external API communication, and inter-service communication, any Java implementations of Obelisk
 * services, applications and SDKs use this common RPC (Remote Procedure Call) package. It provides a singular framework
 * that can be used with several underlying implementations, such as AMQP or HTTP.
 * @see org.burrow_studios.obelisk.commons.rpc.RPCServer
 * @see org.burrow_studios.obelisk.commons.rpc.RPCClient
 * @see org.burrow_studios.obelisk.commons.rpc.RPCRequest
 * @see org.burrow_studios.obelisk.commons.rpc.RPCRequest
 * @see org.burrow_studios.obelisk.commons.rpc.Endpoint
 */
package org.burrow_studios.obelisk.commons.rpc;