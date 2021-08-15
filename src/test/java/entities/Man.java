package entities;

import com.mvgreen.deepcopy.annotations.CopyMode;
import com.mvgreen.deepcopy.annotations.DeepCopyable;

import java.util.List;
import java.util.Objects;

@DeepCopyable
public class Man {
    private String name;
    private int age;
    @CopyMode(CopyMode.Mode.DEEP)
    private List<String> favoriteBooks;

    public Man() {
    }

    public Man(String name, int age, List<String> favoriteBooks) {
        this.name = name;
        this.age = age;
        this.favoriteBooks = favoriteBooks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(List<String> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Man man = (Man) o;
        return age == man.age && Objects.equals(name, man.name) && Objects.equals(favoriteBooks, man.favoriteBooks);
    }

}
