package com.vandamodaintima.jfpsb.contador;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        String[] osArray = { "Contador", "Produtos", "Fornecedores", "Lojas" };
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);

        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = null;
                Class fragmentClass = null;

                switch (i) {
                    case 0:
                        fragmentClass = TelaContador.class;
                        break;
                    case 1:
                        fragmentClass = TelaProduto.class;
                        break;
                    case 2:
                        fragmentClass = TelaFornecedor.class;
                        break;
                    case 3:
                        fragmentClass = TelaLoja.class;
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Opção inválida", Toast.LENGTH_SHORT).show();
                        break;
                }

                try {
                    fragment = (Fragment) fragmentClass.newInstance();

                    // Insert the fragment by replacing any existing fragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                    mDrawerLayout.closeDrawer(mDrawerList);
                } catch (NullPointerException npe) {
                    Toast.makeText(MainActivity.this, "Opção Inválida Foi Selecionada", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
