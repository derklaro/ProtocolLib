/*
 *  ProtocolLib - Bukkit server library that allows access to the Minecraft protocol.
 *  Copyright (C) 2012 Kristian S. Stangeland
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the 
 *  GNU General Public License as published by the Free Software Foundation; either version 2 of 
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program; 
 *  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 *  02111-1307 USA
 */

package com.comphenix.protocol.events;

import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.injector.GamePhase;
import com.google.common.base.Preconditions;

/**
 * Represents a packet listener with useful constructors.
 * <p>
 * Remember to override onPacketReceiving() and onPacketSending(), depending on the ConnectionSide.
 * @author Kristian
 */
public abstract class PacketAdapter implements PacketListener {
	protected Plugin plugin;
	protected ConnectionSide connectionSide;
	protected ListeningWhitelist receivingWhitelist = ListeningWhitelist.EMPTY_WHITELIST;
	protected ListeningWhitelist sendingWhitelist = ListeningWhitelist.EMPTY_WHITELIST;

	/**
	 * Initialize a packet adapter using a collection of parameters. Use {@link #params()} to get an instance to this builder.
	 * @param params - the parameters.
	 */
	public PacketAdapter(@Nonnull AdapterParameteters params) {
		this(
			checkValidity(params).plugin, params.connectionSide, params.listenerPriority, 
			params.gamePhase, params.options, params.packets
		);
	}
	
	/**
	 * Initialize a packet listener with default priority.
	 * @param plugin - the plugin that spawned this listener.
	 * @param connectionSide - the packet type the listener is looking for.
	 * @param packets - the packet IDs the listener is looking for.
	 */
	public PacketAdapter(Plugin plugin, ConnectionSide connectionSide, Integer... packets) {
		this(plugin, connectionSide, ListenerPriority.NORMAL, packets);
	}
	
	/**
	 * Initialize a packet listener for a single connection side.
	 * @param plugin - the plugin that spawned this listener.
	 * @param connectionSide - the packet type the listener is looking for.
	 * @param listenerPriority - the event priority.
	 * @param packets - the packet IDs the listener is looking for.
	 */
	public PacketAdapter(Plugin plugin, ConnectionSide connectionSide, ListenerPriority listenerPriority, Set<Integer> packets) {
		this(plugin, connectionSide, listenerPriority, GamePhase.PLAYING, packets.toArray(new Integer[0]));
	}
	
	/**
	 * Initialize a packet listener for a single connection side.
	 * <p>
	 * The game phase is used to optimize performance. A listener should only choose BOTH or LOGIN if it's absolutely necessary.
	 * @param plugin - the plugin that spawned this listener.
	 * @param connectionSide - the packet type the listener is looking for.
	 * @param gamePhase - which game phase this listener is active under.
	 * @param packets - the packet IDs the listener is looking for.
	 */
	public PacketAdapter(Plugin plugin, ConnectionSide connectionSide, GamePhase gamePhase, Set<Integer> packets) {
		this(plugin, connectionSide, ListenerPriority.NORMAL, gamePhase, packets.toArray(new Integer[0]));
	}
	
	/**
	 * Initialize a packet listener for a single connection side.
	 * <p>
	 * The game phase is used to optimize performance. A listener should only choose BOTH or LOGIN if it's absolutely necessary.
	 * @param plugin - the plugin that spawned this listener.
	 * @param connectionSide - the packet type the listener is looking for.
	 * @param listenerPriority - the event priority.
	 * @param gamePhase - which game phase this listener is active under.
	 * @param packets - the packet IDs the listener is looking for.
	 */
	public PacketAdapter(Plugin plugin, ConnectionSide connectionSide, ListenerPriority listenerPriority, GamePhase gamePhase, Set<Integer> packets) {
		this(plugin, connectionSide, listenerPriority, gamePhase, packets.toArray(new Integer[0]));
	}
	
	/**
	 * Initialize a packet listener for a single connection side.
	 * @param plugin - the plugin that spawned this listener.
	 * @param connectionSide - the packet type the listener is looking for.
	 * @param listenerPriority - the event priority.
	 * @param packets - the packet IDs the listener is looking for.
	 */
	public PacketAdapter(Plugin plugin, ConnectionSide connectionSide, ListenerPriority listenerPriority, Integer... packets) {
		this(plugin, connectionSide, listenerPriority, GamePhase.PLAYING, packets);
	}
	
	/**
	 * Initialize a packet listener for a single connection side.
	 * @param plugin - the plugin that spawned this listener.
	 * @param connectionSide - the packet type the listener is looking for.
	 * @param options - which listener options to use.
	 * @param packets - the packet IDs the listener is looking for.
	 */
	public PacketAdapter(Plugin plugin, ConnectionSide connectionSide, ListenerOptions[] options, Integer... packets) {
		this(plugin, connectionSide, ListenerPriority.NORMAL, GamePhase.PLAYING, options, packets);
	}
	
	/**
	 * Initialize a packet listener for a single connection side.
	 * @param plugin - the plugin that spawned this listener.
	 * @param connectionSide - the packet type the listener is looking for.
	 * @param gamePhase - which game phase this listener is active under.
	 * @param packets - the packet IDs the listener is looking for.
	 */
	public PacketAdapter(Plugin plugin, ConnectionSide connectionSide, GamePhase gamePhase, Integer... packets) {
		this(plugin, connectionSide, ListenerPriority.NORMAL, gamePhase, packets);
	}
	
	/**
	 * Initialize a packet listener for a single connection side.
	 * <p>
	 * The game phase is used to optimize performance. A listener should only choose BOTH or LOGIN if it's absolutely necessary.
	 * @param plugin - the plugin that spawned this listener.
	 * @param connectionSide - the packet type the listener is looking for.
	 * @param listenerPriority - the event priority.
	 * @param gamePhase - which game phase this listener is active under.
	 * @param packets - the packet IDs the listener is looking for.
	 */
	public PacketAdapter(Plugin plugin, ConnectionSide connectionSide, ListenerPriority listenerPriority, GamePhase gamePhase, Integer... packets) {
		this(plugin, connectionSide, listenerPriority, gamePhase, new ListenerOptions[0], packets);
	}
	
	/**
	 * Initialize a packet listener for a single connection side.
	 * <p>
	 * The game phase is used to optimize performance. A listener should only choose BOTH or LOGIN if it's absolutely necessary.
	 * <p>
	 * Listener options must be specified in order for {@link NetworkMarker#getInputBuffer()} to function correctly.
	 * @param plugin - the plugin that spawned this listener.
	 * @param connectionSide - the packet type the listener is looking for.
	 * @param listenerPriority - the event priority.
	 * @param gamePhase - which game phase this listener is active under.
	 * @param options - which listener options to use.
	 * @param packets - the packet IDs the listener is looking for.
	 */
	public PacketAdapter(Plugin plugin, ConnectionSide connectionSide, ListenerPriority listenerPriority, GamePhase gamePhase, ListenerOptions[] options, Integer... packets) {
		if (plugin == null)
			throw new IllegalArgumentException("plugin cannot be null");
		if (connectionSide == null)
			throw new IllegalArgumentException("connectionSide cannot be null");
		if (listenerPriority == null)
			throw new IllegalArgumentException("listenerPriority cannot be null");
		if (gamePhase == null)
			throw new IllegalArgumentException("gamePhase cannot be NULL");
		if (packets == null)
			throw new IllegalArgumentException("packets cannot be null");
		if (options == null)
			throw new IllegalArgumentException("options cannot be null");
		
		// Add whitelists
		if (connectionSide.isForServer())
			sendingWhitelist = new ListeningWhitelist(listenerPriority, packets, gamePhase, options);
		if (connectionSide.isForClient())
			receivingWhitelist = new ListeningWhitelist(listenerPriority, packets, gamePhase, options);
		
		this.plugin = plugin;
		this.connectionSide = connectionSide;
	}
	
	@Override
	public void onPacketReceiving(PacketEvent event) {
		// Lets prevent some bugs
		throw new IllegalStateException("Override onPacketReceiving to get notifcations of received packets!");
	}
	
	@Override
	public void onPacketSending(PacketEvent event) {
		// Lets prevent some bugs
		throw new IllegalStateException("Override onPacketSending to get notifcations of sent packets!");
	}
	
	@Override
	public ListeningWhitelist getReceivingWhitelist() {
		return receivingWhitelist;
	}
	
	@Override
	public ListeningWhitelist getSendingWhitelist() {
		return sendingWhitelist;
	}
	
	@Override
	public Plugin getPlugin() {
		return plugin;
	}
	
	/**
	 * Retrieves the name of the plugin that has been associated with the listener.
	 * @param listener - the listener.
	 * @return Name of the associated plugin.
	 */
	public static String getPluginName(PacketListener listener) {
		return getPluginName(listener.getPlugin());
	}
	
	/**
	 * Retrieves the name of the given plugin.
	 * @param plugin - the plugin.
	 * @return Name of the given plugin.
	 */
	public static String getPluginName(Plugin plugin) {
		// Try to get the plugin name
		try {
			if (plugin == null)
				return "UNKNOWN";
			else
				return plugin.getName();
			
		} catch (NoSuchMethodError e) {
			return plugin.toString();
		}
	}
	
	@Override
	public String toString() {		
		// This is used by the error reporter 
		return String.format("PacketAdapter[plugin=%s, sending=%s, receiving=%s]", 
				getPluginName(this), 
				sendingWhitelist,
				receivingWhitelist);
	}
	
	/**
	 * Construct a helper object for passing parameters to the packet adapter.
	 * <p>
	 * This is often simpler and better than passing them directly to each constructor.
	 * @return Helper object.
	 */
	public static AdapterParameteters params() {
		return new AdapterParameteters();
	}
	
	/**
	 * Construct a helper object for passing parameters to the packet adapter.
	 * <p>
	 * This is often simpler and better than passing them directly to each constructor.
	 * @param plugin - the plugin that spawned this listener.
	 * @param packets - the packet IDs the listener is looking for.
	 * @return Helper object.
	 */
	public static AdapterParameteters params(Plugin plugin, Integer... packets) {
		return new AdapterParameteters().plugin(plugin).packets(packets);
	}
	
	/**
	 * Represents a builder for passing parameters to the packet adapter constructor.
	 * @author Kristian
	 */
	public static class AdapterParameteters {
		private Plugin plugin;
		private ConnectionSide connectionSide;
		private Integer[] packets;
	
		// Parameters with default values
		private GamePhase gamePhase = GamePhase.PLAYING;
		private ListenerOptions[] options = new ListenerOptions[0];
		private ListenerPriority listenerPriority = ListenerPriority.NORMAL;
		
		/**
		 * Set the plugin that spawned this listener. This parameter is required.
		 * @param plugin - the plugin.
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters plugin(@Nonnull Plugin plugin) {
			this.plugin = Preconditions.checkNotNull(plugin, "plugin cannot be NULL.");
			return this;
		}
		
		/**
		 * Set the packet types this listener is looking for. This parameter is required.
		 * @param connectionSide - the new packet type.
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters connectionSide(@Nonnull ConnectionSide connectionSide) {
			this.connectionSide = Preconditions.checkNotNull(connectionSide, "connectionside cannot be NULL.");
			return this;
		}
		
		/**
		 * Set this adapter to also look for client-side packets. 
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters clientSide() {
			return connectionSide(ConnectionSide.add(connectionSide, ConnectionSide.CLIENT_SIDE));
		}
		
		/**
		 * Set this adapter to also look for client-side packets. 
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters serverSide() {
			return connectionSide(ConnectionSide.add(connectionSide, ConnectionSide.SERVER_SIDE));
		}
		
		/**
		 * Set the the event priority, where the execution is in ascending order from lowest to highest. 
		 * <p>
		 * Default is {@link ListenerPriority#NORMAL}.
		 * @param listenerPriority - the new event priority.
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters listenerPriority(@Nonnull ListenerPriority listenerPriority) {
			this.listenerPriority = Preconditions.checkNotNull(listenerPriority, "listener priority cannot be NULL.");
			return this;
		}
		
		/**
		 * Set which game phase this listener is active under. This is a hint for ProtocolLib to start intercepting login packets.
		 * <p>
		 * Default is {@link GamePhase#PLAYING}, which will not intercept login packets.
		 * @param gamePhase - the new game phase.
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters gamePhase(@Nonnull GamePhase gamePhase) {
			this.gamePhase = Preconditions.checkNotNull(gamePhase, "gamePhase cannot be NULL.");
			return this;
		}
		
		/**
		 * Set the game phase to {@link GamePhase#LOGIN}, allowing ProtocolLib to intercept login packets.
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters loginPhase() {
			return gamePhase(GamePhase.LOGIN);
		}
		
		/**
		 * Set listener options that decide whether or not to intercept the raw packet data as read from the network stream.
		 * <p>
		 * The default is to disable this raw packet interception.
		 * @param options - every option to use.
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters options(@Nonnull ListenerOptions... options) {
			this.options = Preconditions.checkNotNull(options, "options cannot be NULL.");
			return this;
		}
		
		/**
		 * Set the listener option to {@link ListenerOptions#INTERCEPT_INPUT_BUFFER}, causing ProtocolLib to read the raw packet data from the network stream.
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters optionIntercept() {
			return options(ListenerOptions.INTERCEPT_INPUT_BUFFER);
		}
		
		/**
		 * Set the packet IDs of the packets the listener is looking for.
		 * <p>
		 * This parameter is required.
		 * @param packets - the packet IDs to look for.
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters packets(@Nonnull Integer... packets) {
			this.packets = Preconditions.checkNotNull(packets, "packets cannot be NULL");
			return this;
		}
		
		/**
		 * Set the packet IDs of the packets the listener is looking for.
		 * <p>
		 * This parameter is required.
		 * @param packets - a set of the packet IDs to look for.
		 * @return This builder, for chaining.
		 */
		public AdapterParameteters packets(@Nonnull Set<Integer> packets) {
			return packets(packets.toArray(new Integer[0]));
		}
	}
	
	/**
	 * Determine if the required parameters are set.
	 */
	private static AdapterParameteters checkValidity(AdapterParameteters params) {
		if (params == null)
			throw new IllegalArgumentException("params cannot be NULL.");
		if (params.plugin == null)
			throw new IllegalStateException("Plugin was never set in the parameters.");
		if (params.connectionSide == null)
			throw new IllegalStateException("Connection side was never set in the parameters.");
		if (params.packets == null)
			throw new IllegalStateException("Packet IDs was never set in the parameters.");
		return params;
	}
}
