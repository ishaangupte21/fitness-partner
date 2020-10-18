const functions = require('firebase-functions');
const admin = require('firebase-admin')

admin.initializeApp()

const auth = admin.auth()
const db = admin.firestore()


exports.createCoachNode = functions.https.onCall(async (data, context) => {

    try {
        const email = data.email
        const uid = await (await auth.getUserByEmail(email)).uid
        const fn = data.fn
        const ln = data.ln
        const joined = data.dateJoined
        const classes = []

        const newUser = await auth.updateUser(uid, { displayName: `${fn}${ln}` })

        console.log(`display name updated for ${newUser} who is a coach`)

        await auth.setCustomUserClaims(uid, { coach: true })

        console.log('custom claim set to true')

        await db.collection('userdata').doc('accountNodes').collection('coaches').doc(uid).set({
            firstName: fn,
            lastName: ln,
            emailAddress: email,
            dateJoined: joined,
            classes: classes
        })
        console.log(`Node for ${fn} ${ln} created who is a coach`)
        return `Node for ${fn} ${ln} created who is a coach`

    } catch (err) {
        console.error(`Error: ${err.message}`);
        return err;
    }



})


exports.createAthleteNode = functions.https.onCall(async (data, context) => {
    try {
        const email = data.email
        let uid;
        const fn = data.fn
        const ln = data.ln
        const joined = data.dateJoined
        const classes = []

        const user = await auth.getUserByEmail(email)
        uid = user.uid


        const newUser = await auth.updateUser(uid, { displayName: `${fn}${ln}` })

        console.log(`display name updated for ${newUser} who is an athlete`)


        await auth.setCustomUserClaims(uid, { coach: false })
        console.log('custom claim set to false')




        await db.collection('userdata').doc('accountNodes').collection('athletes').doc(uid).set({
            firstName: fn,
            lastName: ln,
            emailAddress: email,
            dateJoined: joined,
            enrolledClasses: classes,
            workoutsCompleted: [],
            completedSize: 0
        })

        console.log(`Node for ${fn} ${ln} created who is an athlete`)
        return `Node for ${fn} ${ln} created who is an athlete`

    } catch (err) {
        console.error(`Error: ${err.message}`)
        return err
    }
})


exports.deleteUserNode = functions.auth.user().onDelete(async user => {
    try {
        const uid = user.uid;

        const doc = await db.collection('userdata').doc('accountNodes').collection('athletes').doc(uid).get()

        if (doc.exists) {
            await db.collection('userdata').doc('accountNodes').collection('athletes').doc(uid).delete()
            console.log(`Doc for user with uid ${uid} who was a coach is deleted`)
            return `Doc for user with uid ${uid} who was not a coach is deleted`
        } else {
            await db.collection('userdata').doc('accountNodes').collection('coaches').doc(uid).delete()
            console.log(`Doc for user with uid ${uid} who was a coach is deleted`)
            return `Doc for user with uid ${uid} who was a coach is deleted`
        }
    } catch (err) {
        console.error(err)
        return `Error: ${err.message}`
    }
})

exports.addAthleteToClass = functions.https.onCall(async (data, context) => {
    try {
        const uid = data.uid
        const classID = data.classId
        const userRef = admin.firestore().collection('userdata').doc('accountNodes').collection('athletes').doc(uid)
        const doc = await userRef.get();
        if (doc.data().enrolledClasses.includes(classID)) {
            throw new functions.https.HttpsError(
                'failed-precondition',
                'You are already part of this Class!'
            )


        }

        if (!context.auth) {
            throw new functions.https.HttpsError(
                'unauthenticated',
                'You must be authenticated to join a class'
            )
        }

        //add to classList

        const classRef = db.collection('classes').doc(classID);


        const classDoc = await classRef.get();

        if (!classDoc.exists) {
            throw new functions.https.HttpsError(
                'invalid-argument',
                'class with that ID does not exist'
            )

        }

        await classRef.update({
            athletes: admin.firestore.FieldValue.arrayUnion(uid)
        });
        console.log('updated class');
        //update in user node

        await userRef.update({
            enrolledClasses: admin.firestore.FieldValue.arrayUnion(classID)
        });

        return 'Successfully Joined Class'

    } catch (err) {
        console.error(err.message);
        return err.message;
    }


})


exports.undoAddAthlete = functions.https.onCall(async (data, context) => {
    try {
        if (!context.auth) {
            throw new functions.https.HttpsError(
                'unauthenticated',
                'you must be authenticated to do this'
            )
        }

        const uid = data.uid;
        const classID = data.classId;


        await db.collection('classes').doc(classID).update({ athletes: admin.firestore.FieldValue.arrayRemove(uid) });
        console.log('removed from class');
        await db.collection('userdata').doc('accountNodes').collection('athletes').doc(uid).update({
            enrolledClasses: admin.firestore.FieldValue.arrayRemove(classID)
        });
        console.log('removed from user node');

        return 'Undo Successful'

    } catch (err) {
        console.error(err);
        return err.message;
    }

})

exports.addWorkout = functions.https.onCall(async (data, context) => {
    try {
        if (!context.auth) {
            throw new functions.https.HttpsError(
                'unauthenticated',
                'You must be authenticated for this action'
            )
        }

        if (context.auth.token.coach !== true) {
            throw new functions.https.HttpsError(
                'permission-denied',
                'You must be a coach to perform this action'
            )
        }

        const name = data.name;
        const exercise = data.exercise;
        const amount = data.amount;
        const reps = data.reps;
        const date = data.date;
        const className = data.className;
        const parentClass = data.parentClass;

        let classId;

        const classSnap = await db.collection('classes').where('name', '==', className).get();
        classSnap.forEach(groupClass => {
            classId = groupClass.id;
        })


        const workoutSnap = await admin.firestore().collection('workouts').where('name', '==', name).get();

        workoutSnap.forEach(workout => {
            if (name == workout.data().name) {
                throw new functions.https.HttpsError(
                    'already-exists',
                    'Workout with this name already exists'
                )
            }
        });

        const doc = await db.collection('workouts').add({ name, exercise, amount, reps, date, parentClass, completedBy: [] });
        await db.collection('classes').doc(classId).update({ workouts: admin.firestore.FieldValue.arrayUnion(doc.id) });

        return 'Workout successfully created';


    } catch (err) {
        throw new functions.https.HttpsError(
            'internal',
            err.message
        )
    }
})

exports.deleteWorkout = functions.https.onCall(async (data, context) => {
    
})

exports.completeWorkout = functions.https.onCall(async (data, context) => {
    try {
        if (!context.auth) {
            throw new functions.https.HttpsError(
                'unauthenticated',
                'You must be authenticated for this action'
            )
        }
        const uid = context.auth.uid
        const workoutId = data.workoutId

        await db.collection('workouts').doc(workoutId).update({
            completedBy: admin.firestore.FieldValue.arrayUnion(uid)
        })

        return db.collection('userdata').doc('accountNodes').collection('athletes').doc(uid).update({
            workoutsCompleted: admin.firestore.FieldValue.arrayUnion(workoutId),
            completedSize: admin.firestore.FieldValue.increment(1)
        })

    } catch (err) {
        throw new functions.https.HttpsError(
            'internal',
            err.message
        )
    }
})

exports.workoutIncomplete = functions.https.onCall(async (data, context) => {
    try {
        if (!context.auth) {
            throw new functions.https.HttpsError(
                'unauthenticated',
                'You must be authenticated for this action'
            )
        }
        const uid = context.auth.uid
        const workoutId = data.workoutId
        await db.collection('workouts').doc(workoutId).update({
            completedBy: admin.firestore.FieldValue.arrayRemove(uid)
        })
    
        return db.collection('userdata').doc('accountNodes').collection('athletes').doc(uid).update({
            workoutsCompleted: admin.firestore.FieldValue.arrayRemove(workoutId),
            completedSize: admin.firestore.FieldValue.increment(-1)
        })
    } catch (err) {
        throw new functions.https.HttpsError(
            'internal',
            err.message
        )
    }
})

exports.unenrollFromClass = functions.https.onCall(async (data, context) => {
    try {
        if (!context.auth) {
            throw new functions.https.HttpsError(
                'unauthenticated',
                'You must be authenticated for this action'
            )
        }

        if (context.auth.token.coach !== true) {
            throw new functions.https.HttpsError(
                'permission-denied',
                'You must be a coach to perform this action'
            )
        }

        const uid = context.auth.uid
        const classId = (await db.collection('classes').where('name', '==', data.className).limit(1).get()).docs[0].id

        await db.collection('classes').doc(classId).update({
            athletes: admin.firestore.FieldValue.arrayRemove(uid)
        })

        return db.collection('userdata').doc('accountNodes').collection('athletes').doc(uid).update({
            enrolledClasses: admin.firestore.FieldValue.arrayRemove(classId)
        })

    } catch (err) {
        throw new functions.https.HttpsError(
            'internal',
            err.message
        )
    }
})

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
