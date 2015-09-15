package com.gemstone.gemfire.distributed.internal.membership.gms.interfaces;

import java.io.NotSerializableException;
import java.util.Collection;
import java.util.Set;

import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.distributed.internal.DistributionMessage;
import com.gemstone.gemfire.distributed.internal.membership.InternalDistributedMember;
import com.gemstone.gemfire.distributed.internal.membership.NetMember;
import com.gemstone.gemfire.distributed.internal.membership.NetView;
import com.gemstone.gemfire.distributed.internal.membership.gms.SuspectMember;

/**
 * Manager presents the GMS services to the outside world and
 * handles startup/shutdown race conditions.  It is also the
 * default MessageHandler
 */
public interface Manager extends Service, MessageHandler {

  /**
   * After all services have been started this is used to
   * join the distributed system
   */
  void joinDistributedSystem();

  /**
   * Sends a message using a selected distribution channel
   * (e.g. Messenger or DirectChannel)
   * @return a set of recipients that did not receive the message
   */
  Set<InternalDistributedMember> send(DistributionMessage m) throws NotSerializableException;

  /**
   * initiates a Forced Disconnect, shutting down the distributed system
   * and closing the cache
   * @param reason
   */
  void forceDisconnect(String reason);
  
  /**
   * notifies the manager that membership quorum has been lost
   */
  void quorumLost(Collection<InternalDistributedMember> failures, NetView view);

  /**
   * Notifies the manager that a member has contacted us who is not in the
   * current membership view
   * @param mbr
   * @param birthTime
   */
  void addSurpriseMemberForTesting(DistributedMember mbr, long birthTime);

  /**
   * Tests to see if the given member has been put into "shunned" state,
   * meaning that it has left the distributed system and we should no longer
   * process requests from it.  Shunned status eventually times out.
   * @param mbr
   * @return true if the member is shunned
   */
  boolean isShunned(DistributedMember mbr);

  /**
   * returns the lead member from the current membership view.  This is
   * typically the oldest member that is not an Admin or Locator member.
   * @return the ID of the lead member
   */
  DistributedMember getLeadMember();

  /**
   * returns the coordinator of the current membership view.  This is
   * who created and distributed the view.  See NetView.
   * @return
   */
  DistributedMember getCoordinator();
  
  /**
   * sometimes we cannot perform multicast messaging, such as during a
   * rolling upgrade.
   * @return true if multicast messaging can be performed
   */
  boolean isMulticastAllowed();
  
  /**
   * This establishes the reason that a shutdown is being performed.
   * After this the cancelCriterion will start reporting that a
   * cancel is in progress.
   * @param e the reason for the shutdown
   */
  void setShutdownCause(Exception e);
  
  /**
   * Returns the reason for a shutdown. 
   */
  Throwable getShutdownCause();
  
  /**
   * Returns true if a shutdown is in progress or has been completed
   */
  boolean shutdownInProgress();

  /**
   * similar to forceDisconnect but is used solely by Messenger
   * to tell Manager that communications have been lost
   */
  void membershipFailure(String message, Exception cause);

  /**
   * used by HealthMonitor to tell Manager that a member is under
   * suspicion
   */
  void memberSuspected(SuspectMember suspect);
}