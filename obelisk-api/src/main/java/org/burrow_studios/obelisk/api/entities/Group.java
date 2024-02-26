package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.group.GroupModifier;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.jetbrains.annotations.NotNull;

/**
 * A group is a collection of {@link User Users} with collective attributes. Groups can be used to categorize Users or
 * to simplify permission handling and access control.
 */
public interface Group extends Turtle {
    @Override
    @NotNull GroupModifier modify();

    @Override
    @NotNull DeleteAction<Group> delete();

    /**
     * Provides the name of this Group. Group names are not guaranteed to be unique and rather function as a description.
     * @return The Group name.
     */
    @NotNull String getName();

    /**
     * Provides the amount of members within this group.
     * @return Amount of members.
     */
    int getSize();

    /**
     * Provides a List of all {@link User Users} that are a member of this Group.
     * <br> A Group can have zero or more Users; A User can also be part of zero or more Groups.
     * @return List of members.
     */
    @NotNull TurtleSetView<? extends User> getMembers();

    /**
     * Creates an Action with the instruction to add the provided User to the list of Group members.
     * @param user A User
     * @return Action that provides the modified Group on completion.
     */
    @NotNull Action<Group> addMember(@NotNull User user);

    /**
     * Creates an Action with the instruction to remove the provided User from the list of Group members.
     * @param user A User
     * @return Action that provides the modified Group on completion.
     */
    @NotNull Action<Group> removeMember(@NotNull User user);

    /**
     * Provides the position attribute of this Group. The position of a Group may be used in lists to rank certain Groups
     * above or below others, overriding whatever sorting they would otherwise have (usually alphabetically).
     * @return The Group position attribute.
     */
    int getPosition();
}
