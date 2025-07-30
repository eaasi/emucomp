/*
 * This file is part of the Emulation-as-a-Service framework.
 *
 * The Emulation-as-a-Service framework is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * The Emulation-as-a-Service framework is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Emulation-as-a-Software framework.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package de.bwl.bwfla.emucomp.xpra;

import de.bwl.bwfla.emucomp.common.logging.PrefixLogger;

import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * PulseAudio streamer for Xpra - WebRTC functionality has been disabled.
 * Audio is now handled natively by Xpra using PulseAudio.
 * <p>
 * Original WebRTC code was based on GStreamer's WebRTC demos:
 * https://github.com/centricular/gstwebrtc-demos
 */
public class PulseAudioStreamer implements IAudioStreamer {
    private final Logger log;
    private final Path pulsesock;
    private final BlockingQueue<String> outqueue;

    // WebRTC components commented out - using Xpra native audio instead
	/*
	private final Pipeline pipeline;
	private final WebRTCBin webrtc;
	private final Bin audio;
	*/

    private boolean closed;
    private boolean playing;

    private static final long OUTPUT_QUEUE_OFFER_TIMEOUT = 5L;


    public PulseAudioStreamer(String cid, Path pulsesock) {
        final PrefixLogger logger = new PrefixLogger(PulseAudioStreamer.class.getName());
        logger.getContext().add("cid", cid);
        this.log = logger;
        this.pulsesock = pulsesock;

        log.info("Initializing PulseAudioStreamer for Xpra native audio (WebRTC disabled)");

        // WebRTC initialization commented out - not needed for Xpra
		/*
		try {
			Gst.init(new Version(1, 14));
		}
		catch (Exception error) {
			log.log(Level.SEVERE, "Initializing GStreamer failed!", error);
			throw error;
		}
		*/

        this.outqueue = new ArrayBlockingQueue<>(16);

        // WebRTC pipeline creation commented out - Xpra handles audio natively
		/*
		this.pipeline = PulseAudioStreamer.createPipeline(log);
		this.audio = PulseAudioStreamer.createAudioBin(pulsesock.toString());
		this.webrtc= PulseAudioStreamer.createWebRtcBin(pipeline, outqueue, log);
		*/

        this.closed = false;
        this.playing = false;

        // WebRTC pipeline setup commented out - not needed for Xpra
		/*
		pipeline.addMany(webrtc, audio);
		audio.link(webrtc);
		*/

        log.info("PulseAudioStreamer initialized for Xpra native audio at: " + pulsesock);
    }

    @Override
    public String pollServerControlMessage(long timeout, TimeUnit unit) {
        // WebRTC control messages not needed for Xpra native audio
        // Return null to indicate no WebRTC signaling messages
        log.fine("pollServerControlMessage called - WebRTC disabled, returning null for Xpra");
        return null;
		
		/* WebRTC implementation commented out:
		try {
			return outqueue.poll(timeout, unit);
		}
		catch (InterruptedException error) {
			return null;  // Ignore it!
		}
		*/
    }

    @Override
    public void postClientControlMessage(char[] payload) throws IllegalArgumentException {
        this.postClientControlMessage(payload, 0, payload.length);
    }

    @Override
    public void postClientControlMessage(char[] payload, int offset, int length) throws IllegalArgumentException {
        this.postClientControlMessage(new String(payload, offset, length));
    }

    @Override
    public void postClientControlMessage(String payload) throws IllegalArgumentException {
        // WebRTC control message handling commented out - not needed for Xpra native audio
        log.fine("postClientControlMessage called with WebRTC disabled, using Xpra: " + payload);
		
		/* WebRTC implementation commented out:
		final ControlMessage message = ControlMessage.parse(payload);
		final String msgtype = message.getType();
		switch (msgtype) {
			case ControlMessage.Types.SDP:
				log.info("SDP answer received");
				final SDPMessage sdpmsg = new SDPMessage();
				sdpmsg.parseBuffer(((SdpData) message.getData()).getSdpMessage());
				webrtc.setRemoteDescription(new WebRTCSessionDescription(WebRTCSDPType.ANSWER, sdpmsg));
				break;

			case ControlMessage.Types.ICE:
				final IceData ice = (IceData) message.getData();
				final String candidate = ice.getCandidate();
				log.info("ICE candidate received: " + candidate);
				webrtc.addIceCandidate(ice.getSdpMLineIndex(), candidate);
				break;

			default:
				throw new IllegalArgumentException("Unknown message type: " + msgtype);
		}
		*/
    }

    @Override
    public void play() {
        log.info("Starting PulseAudioStreamer for Xpra native audio...");
        playing = true;

        // WebRTC pipeline start commented out - Xpra handles audio natively
		/*
		pipeline.play();
		*/

        log.info("PulseAudioStreamer started - audio handled natively by Xpra");
    }

    @Override
    public void stop() {
        log.info("Stopping PulseAudioStreamer...");
        playing = false;

        // WebRTC pipeline stop commented out - not needed for Xpra
		/*
		pipeline.stop();

		final ControlMessage<EosData> eosmessage = ControlMessage.wrap(new EosData());
		outqueue.offer(eosmessage.toString());
		*/

        log.info("PulseAudioStreamer stopped - Xpra native audio");
    }

    @Override
    public void close() {
        log.info("Closing PulseAudioStreamer...");
        playing = false;
        closed = true;

        // WebRTC cleanup commented out - not needed for Xpra
		/*
		try {
			audio.unlink(webrtc);
			pipeline.remove(webrtc);
			pipeline.remove(audio);
			webrtc.close();
			audio.close();
			pipeline.close();
			closed = true;
			log.info("Closed audio streamer");
		}
		finally {
			Gst.quit();
		}
		*/

        log.info("PulseAudioStreamer closed - Xpra native audio");
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    public boolean isPlaying() {
        return playing;
    }

    public Path getPulseSocketPath() {
        return pulsesock;
    }


    // ========== WebRTC Internal Helpers - ALL COMMENTED OUT ==============================

	/* WebRTC pipeline creation commented out - not needed for Xpra:
	
	private static Pipeline createPipeline(Logger log)
	{
		final Bus.STATE_CHANGED onStateChanged = (source, old, current, pending) -> {
			if (source instanceof Pipeline)
				log.info("Pipeline state changed: " + old + " --> " + current);
		};

		final Bus.EOS onEndOfStream = (source) -> {
			log.info("Reached end of stream: " + source.toString());
			Gst.quit();
		};

		final Bus.ERROR onError = (source, code, message) -> {
			log.severe("Error " + code + " from '" + source + "'. " + message);
		};

		final Bus.WARNING onWarning = (source, code, message) -> {
			log.warning("Warning " + code + " from '" + source + "'. " + message);
		};

		final Bus.INFO onInfo = (source, code, message) -> {
			log.info("Info " + code + " from '" + source + "'. " + message);
		};

		final Pipeline pipeline = new Pipeline();
		final Bus bus = pipeline.getBus();
		bus.connect(onStateChanged);
		bus.connect(onEndOfStream);
		bus.connect(onError);
		bus.connect(onWarning);
		bus.connect(onInfo);
		return pipeline;
	}

	private static WebRTCBin createWebRtcBin(Pipeline pipeline, BlockingQueue<String> outqueue, Logger log)
	{
		final WebRTCBin webrtc = new WebRTCBin("webrtc");
		webrtc.setStunServer("stun://stun.l.google.com:19302");

		final var pipelineWeakRef = new WeakReference<>(pipeline);
		final var webrtcWeakRef = new WeakReference<>(webrtc);

		final CREATE_OFFER onOfferCreated = (offer) -> {
			final var webrtcLocalRef = webrtcWeakRef.get();
			if (webrtcLocalRef == null)
				return;

			webrtcLocalRef.setLocalDescription(offer);

			final SdpData sdp = new SdpData(SdpData.Types.OFFER, offer.getSDPMessage().toString());
			final ControlMessage<SdpData> message = ControlMessage.wrap(sdp);
			try {
				log.info("Sending SDP-offer...");
				outqueue.offer(message.toString(), OUTPUT_QUEUE_OFFER_TIMEOUT, TimeUnit.SECONDS);
			}
			catch (InterruptedException error) {
				log.log(Level.WARNING, "Sending SDP-offer failed!", error);
			}
		};

		final ON_NEGOTIATION_NEEDED onNegotiationNeeded = (elem) -> {
			final var pipelineLocalRef = pipelineWeakRef.get();
			if (pipelineLocalRef == null)
				return;

			final var webrtcLocalRef = webrtcWeakRef.get();
			if (webrtcLocalRef == null)
				return;

			log.info("Negotiation needed, waiting for pipeline to start playing...");

			while (!pipelineLocalRef.isPlaying()) {
				try {
					Thread.sleep(1000L);
				}
				catch (Exception error) {
					// Ignore it!
				}
			}

			log.info("Creating SDP-offer...");
			webrtcLocalRef.createOffer(onOfferCreated);
		};

		final ON_ICE_CANDIDATE onIceCandidate = (sdpMLineIndex, candidate) -> {
			final IceData ice = new IceData(candidate, sdpMLineIndex);
			final ControlMessage<IceData> message = ControlMessage.wrap(ice);
			try {
				log.info("Sending ICE-candidate: " + candidate);
				outqueue.offer(message.toString(), OUTPUT_QUEUE_OFFER_TIMEOUT, TimeUnit.SECONDS);
			}
			catch (InterruptedException error) {
				log.log(Level.WARNING, "Sending ICE-candidate failed!", error);
			}
		};

		webrtc.connect(onNegotiationNeeded);
		webrtc.connect(onIceCandidate);

		return webrtc;
	}

	private static Bin createAudioBin(String pulsesock)
	{
		final String description = "pulsesrc server=" + pulsesock + " device=emu-speaker.monitor "
				+ "! audioconvert ! audioresample ! queue ! opusenc ! rtpopuspay ! queue "
				+ "! capsfilter caps=application/x-rtp,media=audio,encoding-name=OPUS,payload=96";

		return Gst.parseBinFromDescription(description, true);
	}
	
	*/
}


// ========== WebRTC Control Message Classes - ALL COMMENTED OUT ==============================

/* WebRTC control message classes commented out - not needed for Xpra:

interface JsonSerializable
{
	JsonObject toJson();
}


class ControlMessage<T extends JsonSerializable> implements JsonSerializable
{
	static class Types
	{
		static final String SDP = "sdp";
		static final String ICE = "ice";
		static final String EOS = "eos";
	}

	private String type;
	private T data;

	private ControlMessage(String type, T data)
	{
		this.type = type;
		this.data = data;
	}

	String getType()
	{
		return type;
	}

	ControlMessage<T> setType(String type)
	{
		this.type = type;
		return this;
	}

	T getData()
	{
		return data;
	}

	ControlMessage<T> setData(T data)
	{
		this.data = data;
		return this;
	}

	@Override
	public JsonObject toJson()
	{
		return Json.createObjectBuilder()
				.add("type", type)
				.add("data", data.toJson())
				.build();
	}

	@Override
	public String toString()
	{
		return this.toJson().toString();
	}

	static ControlMessage parse(String json)
	{
		try (final JsonReader reader = Json.createReader(new StringReader(json))) {
			final JsonObject message = reader.readObject();
			final String msgtype = message.getString("type");
			final JsonObject data = message.getJsonObject("data");
			switch (msgtype) {
				case Types.SDP:
					return new ControlMessage<>(msgtype, new SdpData(data));

				case Types.ICE:
					return new ControlMessage<>(msgtype, new IceData(data));
			}
		}

		throw new IllegalArgumentException("Invalid JSON message!");
	}

	static ControlMessage<SdpData> wrap(SdpData sdp)
	{
		return new ControlMessage<>(Types.SDP, sdp);
	}

	static ControlMessage<IceData> wrap(IceData ice)
	{
		return new ControlMessage<>(Types.ICE, ice);
	}

	static ControlMessage<EosData> wrap(EosData eos)
	{
		return new ControlMessage<>(Types.EOS, eos);
	}
}


class SdpData implements JsonSerializable
{
	static class Types
	{
		static final String OFFER  = "offer";
		static final String ANSWER = "answer";
	}

	private String type;
	private String sdpmsg;

	SdpData(String type, String sdpmsg)
	{
		this.type = type;
		this.sdpmsg = sdpmsg;
	}

	SdpData(JsonObject json)
	{
		this(json.getString("type"), json.getString("sdp"));
	}

	String getType()
	{
		return type;
	}

	SdpData setType(String type)
	{
		this.type = type;
		return this;
	}

	String getSdpMessage()
	{
		return sdpmsg;
	}

	SdpData setSdpMessage(String sdpmsg)
	{
		this.sdpmsg = sdpmsg;
		return this;
	}

	@Override
	public JsonObject toJson()
	{
		return Json.createObjectBuilder()
				.add("type", type)
				.add("sdp", sdpmsg)
				.build();
	}

	@Override
	public String toString()
	{
		return this.toJson().toString();
	}
}


class IceData implements JsonSerializable
{
	static class Types
	{
		static final String OFFER  = "offer";
		static final String ANSWER = "answer";
	}

	private String candidate;
	private int sdpMLineIndex;

	IceData(String candidate, int sdpMLineIndex)
	{
		this.candidate = candidate;
		this.sdpMLineIndex = sdpMLineIndex;
	}

	IceData(JsonObject json)
	{
		this(json.getString("candidate"), json.getInt("sdpMLineIndex"));
	}

	String getCandidate()
	{
		return candidate;
	}

	IceData setCandidate(String candidate)
	{
		this.candidate = candidate;
		return this;
	}

	int getSdpMLineIndex()
	{
		return sdpMLineIndex;
	}

	IceData setSdpMLineIndex(int index)
	{
		this.sdpMLineIndex = index;
		return this;
	}

	@Override
	public JsonObject toJson()
	{
		return Json.createObjectBuilder()
				.add("candidate", candidate)
				.add("sdpMLineIndex", sdpMLineIndex)
				.build();
	}

	@Override
	public String toString()
	{
		return this.toJson().toString();
	}
}


class EosData implements JsonSerializable
{
	EosData()
	{
		// Empty!
	}

	@Override
	public JsonObject toJson()
	{
		return Json.createObjectBuilder()
				.build();
	}

	@Override
	public String toString()
	{
		return this.toJson().toString();
	}
}

*/
