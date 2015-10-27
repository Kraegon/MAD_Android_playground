package nl.reupload.guube.contactcardapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, RandomUserTask.OnRandomUserAvailable  {

    ListView mPersonListView;
    PersonAdapter mPersonAdapter;
    ArrayList mPersonList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        mPersonListView = (ListView) findViewById(R.id.personListView);

        // Koppel list aan
        mPersonAdapter = new PersonAdapter(this,
                getLayoutInflater(),
                mPersonList);
        mPersonListView.setAdapter(mPersonAdapter);

        // Activate adapter, kan dan ook in een button, of network update
        mPersonAdapter.notifyDataSetChanged();

        // Enable listener
        mPersonListView.setOnItemClickListener(this);
    }

    //
    // Click on selected item in list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(), PersonDetail.class);
        Person contact = (Person) mPersonAdapter.getItem(position);
        i.putExtra("FIRSTNAME", contact.firstName);
        i.putExtra("SURNAME", contact.surname);
        i.putExtra("EMAIL", contact.email);
        i.putExtra("NATIONALITY", contact.nationality);
        i.putExtra("IMAGE", contact.imageURLHigh);
        startActivity(i);
    }

    public void onClick(View v) {
        RandomUserTask getRandomUser = new RandomUserTask(this);
        String[] urls = new String[] { "https://randomuser.me/api/" };
        getRandomUser.execute(urls);

    }

    @Override
    public void onRandomUserAvailable(Person person) {
        // Opslaag in array of mss wel in db?
        mPersonList.add(person);
        mPersonAdapter.notifyDataSetChanged();
    }
}
