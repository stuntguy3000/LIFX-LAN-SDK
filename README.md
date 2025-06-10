# LIFX LAN SDK

----------------

Meet my **LIFX LAN SDK**. This project aims at enabling Java developers to implement use the [LIFX LAN Protocol](https://lan.developer.lifx.com/docs/) to control LIFX devices. This SDK should still be functional in ~mid 2025.

## Methodology
I've run into limitations with existing available Java LIFX LAN SDK's. So, the reasonable thing to do, was to write my own in a week.

Here we are 😊

I've aimed for a 100% feature parity SDK, with no bugs, limitations or shortgaps. It should be simple to use, whilst offering inbuilt reliability (such as UDP packet monitoring for reliable delivery and efficient processing).

Generally speaking, I believe this to be the fastest and most reliable Java SDK currently available. Wi-Fi isn't perfect, neither are IoT devices, but I've been able to build amazing things using my SDK.

**Please Note:** Whilst I've tried my very best to implement the entire API, I do not own every type of LIFX device, and cannot guarantee every single implemented feature works.

Be sure to report any bugs (or fix them!).

You will notice the structure of this SDK internally is laid out to fully match the LIFX LAN Protocol, both in name and structure. This keeps things clean, but can lead to some fun naming conventions. 

Did you know LIFX bulbs are called **Lights** and strips are called **MultiZone**? I sure didn't. 

I'm really proud of my work here. It's the most ambitious Java project I've ever done, and I hope it serves useful for those looking for an SDK to do this.

## Reliability
This SDK implements a carefully designed UDP packet reliability system. LIFX Packets are all sent over UDP, and as such, things get lost from time to time. This can be annoying, especially when lights don't change.

When sending LIFX commands to devices, most API calls include an option to listen for a response. For requests that wait to receive data, this option is not available.

Essentially, the Packet Handler is smart enough to (depending on the desired behavior):
1) Spray and pray packets with no reliability (Maximum speed)
2) Await for a LIFX Acknowledgement message or any form of reply as receipt from the device
   1) This includes automatic timeouts and retries for maximum speed
3) Keep waiting times to an absolute minimum
   1) For example, if we send a packet with a single expected response packet, as soon as we receive that response, we close the socket and move on

These strategies are much smarter than implementing a hard delay of ``xxx`` hundred milliseconds for UDP packets to flow back and forth. That's just time-wasting.

No other Java SDK implements this type of system. As a result, my SDK is **FAST**.

## Implementation & Documentation
Coming soon to a Maven repository near you!
