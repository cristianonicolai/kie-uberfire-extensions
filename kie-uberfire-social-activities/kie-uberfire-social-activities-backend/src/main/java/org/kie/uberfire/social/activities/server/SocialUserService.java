package org.kie.uberfire.social.activities.server;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.bus.server.annotations.Service;
import org.kie.uberfire.social.activities.events.SocialUserFollowedEvent;
import org.kie.uberfire.social.activities.events.SocialUserUnFollowedEvent;
import org.kie.uberfire.social.activities.model.SocialUser;
import org.kie.uberfire.social.activities.service.SocialUserPersistenceAPI;
import org.kie.uberfire.social.activities.service.SocialUserServiceAPI;

@Service
@ApplicationScoped
public class SocialUserService implements SocialUserServiceAPI {

    @Inject
    private Event<SocialUserFollowedEvent> followedEvent;

    @Inject
    private Event<SocialUserUnFollowedEvent> unFollowedEvent;

    @Inject
    @Named("socialUserPersistenceAPI")
    private SocialUserPersistenceAPI socialUserPersistenceAPI;

    @Override
    public void userFollowAnotherUser( String followerUsername,
                                       String followUsername ) {
        SocialUser follower = socialUserPersistenceAPI.getSocialUser( followerUsername );
        SocialUser follow = socialUserPersistenceAPI.getSocialUser( followUsername );
        follower.follow( follow );
        socialUserPersistenceAPI.updateUsers( follower, follow );
        followedEvent.fire( new SocialUserFollowedEvent( follower, follow ) );
    }

    @Override
    public void userUnfollowAnotherUser( String followerUsername,
                                         String followUsername ) {
        SocialUser follower = socialUserPersistenceAPI.getSocialUser( followerUsername );
        SocialUser user = socialUserPersistenceAPI.getSocialUser( followUsername );
        follower.unfollow( user );
        socialUserPersistenceAPI.updateUsers( follower, user );
        unFollowedEvent.fire( new SocialUserUnFollowedEvent( follower, user ) );
    }

    @Override
    public void update( SocialUser... users) {
        socialUserPersistenceAPI.updateUsers( users );
    }

}
